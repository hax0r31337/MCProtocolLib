package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Objects;

public class ScoreMessage extends Message {

    private String name;
    private String objective;
    /** The value displayed instead of the real score (may be null) */
    private String value = "";

    public ScoreMessage(String name, String objective) {
        this.name = name;
        this.objective = objective;
    }

    public ScoreMessage(String name, String objective, String value) {
        this.name = name;
        this.objective = objective;
        this.value = value;
    }

    @Override
    public String getText() {
        return value;
    }

    @Override
    public Message clone() {
        return new ScoreMessage(name, objective, value);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjective() {
        return this.objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public JsonElement toJson() {
        JsonElement e = super.toJson();
        if(e.isJsonObject()) {
            JsonObject jsonParent = e.getAsJsonObject();
            JsonObject json = new JsonObject();
            json.addProperty("name", this.name);
            json.addProperty("objective", this.objective);
            if(!value.equals("")) {
                json.addProperty("value", this.value);
            }
            jsonParent.add("score", json);
            return jsonParent;
        } else {
            return e;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof ScoreMessage)) return false;

        ScoreMessage that = (ScoreMessage) o;
        return super.equals(o) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.objective, that.objective) &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(super.hashCode(), this.name, this.objective, this.value);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
