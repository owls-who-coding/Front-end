package com.example.my_last;

import com.google.gson.annotations.SerializedName;

public class DiseaseTag {
    @SerializedName("disease_number")
    private int diseaseNumber;
    @SerializedName("disease_name")
    private String diseaseName;
    @SerializedName("disease_imfomation_path")
    private String diseaseInformationPath;

    // Getters and setters
}