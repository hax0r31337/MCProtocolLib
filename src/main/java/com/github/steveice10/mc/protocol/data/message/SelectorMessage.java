package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Objects;

public class SelectorMessage extends Message {

    private String selector;

    public SelectorMessage(String selector) {
        this.selector = selector;
    }

    @Override
    public String getText() {
        return selector;
    }

    @Override
    public Message clone() {
        return new SelectorMessage(selector);
    }

    public String getSelector() {
        return this.selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    @Override
    public JsonElement toJson() {
        JsonElement e = super.toJson();
        if(e.isJsonObject()) {
            JsonObject json = e.getAsJsonObject();
            json.addProperty("selector", this.selector);
            return json;
        } else {
            return e;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof SelectorMessage)) return false;

        SelectorMessage that = (SelectorMessage) o;
        return super.equals(o) &&
                Objects.equals(this.selector, that.selector);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(super.hashCode(), this.selector);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
