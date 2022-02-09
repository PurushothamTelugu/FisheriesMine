package com.example.fishriescopy;

public class SetMethods {

    private String fishtype,fingerling_no,total_kgs,total_fingerlings,vehicle;

    public SetMethods(String fishtype, String fingerling_no, String total_kgs, String total_fingerlings, String vehicle) {

        this.fishtype = fishtype;
        this.fingerling_no = fingerling_no;
        this.total_kgs = total_kgs;
        this.total_fingerlings = total_fingerlings;
        this.vehicle = vehicle;
    }

    public String getFishtype() {
        return fishtype;
    }

    public void setFishtype(String fishtype) {
        this.fishtype = fishtype;
    }

    public String getFingerling_no() {
        return fingerling_no;
    }

    public void setFingerling_no(String fingerling_no) {
        this.fingerling_no = fingerling_no;
    }

    public String getTotal_kgs() {
        return total_kgs;
    }

    public void setTotal_kgs(String total_kgs) {
        this.total_kgs = total_kgs;
    }

    public String getTotal_fingerlings() {
        return total_fingerlings;
    }

    public void setTotal_fingerlings(String total_fingerlings) {
        this.total_fingerlings = total_fingerlings;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}
