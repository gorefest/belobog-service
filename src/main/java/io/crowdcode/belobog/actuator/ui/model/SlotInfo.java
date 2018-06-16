package io.crowdcode.belobog.actuator.ui.model;

public class SlotInfo {

    private String title;
    private Integer slotNumber;
    private String styleEnabled;
    private String styleDisabled;
    private boolean isEnabled;
    private boolean isActivated;
    private boolean invertHiLo;
    private Integer pinNumber;

    public Integer getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getStyleEnabled() {
        return styleEnabled;
    }

    public void setStyleEnabled(String styleEnabled) {
        this.styleEnabled = styleEnabled;
    }

    public String getStyleDisabled() {
        return styleDisabled;
    }

    public void setStyleDisabled(String styleDisabled) {
        this.styleDisabled = styleDisabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public boolean isInvertHiLo() {
        return invertHiLo;
    }

    public void setInvertHiLo(boolean invertHiLo) {
        this.invertHiLo = invertHiLo;
    }

    public Integer getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(Integer pinNumber) {
        this.pinNumber = pinNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
