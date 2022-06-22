package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationMessage extends Message {

    private static final Map<String, String> languageMap;
    public static final Pattern STRING_VARIABLE_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    static {
        Map<String, String> langMap = new HashMap<>();
        try {
            Properties prop = new Properties();
            prop.load(TranslationMessage.class.getClassLoader().getResourceAsStream("proto/mc.1.12.lang"));

            for(Map.Entry<Object, Object> entry : prop.entrySet()) {
                langMap.put(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        languageMap = Collections.unmodifiableMap(langMap);
    }

    private String translationKey;
    private Message translationParams[];

    public TranslationMessage(String translationKey, Message... translationParams) {
        this.translationKey = translationKey;
        this.translationParams = translationParams;
        this.translationParams = this.getTranslationParams();
        for(Message param : this.translationParams) {
            param.getStyle().setParent(this.getStyle());
        }
    }

    public String getTranslationKey() {
        if(languageMap.containsKey(this.translationKey)) {
            String format = languageMap.get(this.translationKey);
            Matcher matcher = STRING_VARIABLE_PATTERN.matcher(format);
            int i = 0;
            int j = 0;
            StringBuilder sb = new StringBuilder();

            try {
                for (int l; matcher.find(j); j = l) {
                    int k = matcher.start();
                    l = matcher.end();

                    if (k > j)
                    {
                        sb.append(String.format(format.substring(j, k)));
                    }

                    String s2 = matcher.group(2);
                    String s = format.substring(k, l);

                    if ("%".equals(s2) && "%%".equals(s))
                    {
                        sb.append("%");
                    }
                    else
                    {
                        if (!"s".equals(s2))
                        {
                            throw new IllegalStateException("Unsupported format: '" + s + "'");
                        }

                        String s1 = matcher.group(1);
                        int i1 = s1 != null ? Integer.parseInt(s1) - 1 : i++;

                        if (i1 < this.translationParams.length)
                        {
                            sb.append(this.translationParams[i1].getFullText());
                        }
                    }
                }

                if (j < format.length()) {
                    sb.append(String.format(format.substring(j)));
                }
                return sb.toString();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return this.translationKey;
    }

    public Message[] getTranslationParams() {
        Message copy[] = Arrays.copyOf(this.translationParams, this.translationParams.length);
        for(int index = 0; index < copy.length; index++) {
            copy[index] = copy[index].clone();
        }

        return copy;
    }

    @Override
    public Message setStyle(MessageStyle style) {
        super.setStyle(style);
        for(Message param : this.translationParams) {
            param.getStyle().setParent(this.getStyle());
        }

        return this;
    }

    @Override
    public String getText() {
        return this.translationKey;
    }

    @Override
    public TranslationMessage clone() {
        return (TranslationMessage) new TranslationMessage(this.getTranslationKey(), this.getTranslationParams()).setStyle(this.getStyle().clone()).setExtra(this.getExtra());
    }

    @Override
    public JsonElement toJson() {
        JsonElement e = super.toJson();
        if(e.isJsonObject()) {
            JsonObject json = e.getAsJsonObject();
            json.addProperty("translate", this.translationKey);
            JsonArray params = new JsonArray();
            for(Message param : this.translationParams) {
                params.add(param.toJson());
            }

            json.add("with", params);
            return json;
        } else {
            return e;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof TranslationMessage)) return false;

        TranslationMessage that = (TranslationMessage) o;
        return super.equals(o) &&
                Objects.equals(this.translationKey, that.translationKey) &&
                Arrays.equals(this.translationParams, that.translationParams);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(super.hashCode(), this.translationKey, this.translationParams);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
