package com.example.mynav.ui.BikeCeter

import com.google.gson.annotations.SerializedName

class IbikeData{
    @SerializedName("ID")
    var id: String = ""

    @SerializedName("Position")
    var position: String = ""

    @SerializedName("EName")
    var eName: String = ""

    @SerializedName("X")
    var x: String = ""

    @SerializedName("Y")
    var y: String = ""

    @SerializedName("CArea")
    var cArea: String = ""

    @SerializedName("EArea")
    var eArea: String = ""

    @SerializedName("CAddress")
    var cAddress: String = ""

    @SerializedName("EAddress")
    var eAddress: String = ""

    @SerializedName("AvailableCNT")
    var availableCNT: String = ""

    @SerializedName("EmpCNT")
    var empCNT: String = ""

    @SerializedName("UpdateTime")
    var updateTime: String = ""

}