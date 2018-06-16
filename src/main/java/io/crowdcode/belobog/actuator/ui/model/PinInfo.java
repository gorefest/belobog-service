package io.crowdcode.belobog.actuator.ui.model;

public class PinInfo {

    Integer pin;
    Boolean isEnabled;
    String style;

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
        if (enabled) {
            style="background-color: LightGreen;";
        } else {
            style="background-color: Tomato;";
        }
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
