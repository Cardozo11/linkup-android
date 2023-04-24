package com.example.hirework;

import android.content.Context;

public class GigModel {



    int GigId;
    String GigCategory;

    public String getGigSubCategory() {
        return GigSubCategory;
    }

    public void setGigSubCategory(String gigSubCategory) {
        GigSubCategory = gigSubCategory;
    }

    String GigSubCategory;
    String fUsername;
    String GigTitle;
    String GigDescription;
    String Image1;
    int price;
    int GCD;
    int CompletionDays;
    String Attachment;
    String GigFileFormat;
    String AdditionalInfo;

    public String getFreelancerProfile() {
        return freelancerProfile;
    }

    public void setFreelancerProfile(String freelancerProfile) {
        this.freelancerProfile = freelancerProfile;
    }

    String freelancerProfile;


    // intent for gig

    public final String INTENT_GIG_ID="gId";
    public final String INTENT_GIG_FREELANCER_USERNAME="fuser";
    public final String INTENT_GIG_TITLE="title";
    public final String INTENT_GIG_CATEGORY="category";
    public final String INTENT_GIG_SUB_CATEGORY="subCategory";
    public final String INTENT_GIG_DESCRIPTION="description";
    public final String INTENT_GIG_PRICE="price";
    public final String INTENT_GIG_COMPLETION_DAYS="completionDays";
    public final String INTENT_GIG_ATTACHMENT="attachment";
    public final String INTENT_GIG_FILE_FORMAT="file";
    public final String INTENT_GIG_ADDITIONAL_INFO="additionalInfo";
    public final String INTENT_GIG_IMAGE="img";
    public final String INTENT_GIG_FREELANCER_PROFILE="freelancer_profile";


    // Gig DB KEYS

    public final String DB_FREELANCER_USERNAME="fuser";
    public final String DB_GIG_TITLE="title";
    public final String DB_GIG_CATEGORY="category";
    public final String DB_GIG_SUB_CATEGORY="subcategory";
    public final String DB_GIG_DESCRIPTION="description";
    public final String DB_GIG_PRICE="price";
    public final String DB_GIG_COMPLETION_DAYS="completionDays";
    public final String DB_GIG_ATTACHMENT="attachment";
    public final String DB_GIG_FILE_FORMAT="fileFormat";
    public final String DB_GIG_ADDITIONAL_INFO="additionalinfo";
    public final String DB_GIG_IMAGE="image1";


    Context context;


    public GigModel(Context context){
        this.context=context;
    }



    public GigModel(int gigId, String fUsername, String gigTitle, String gigDescription, String image1, int price, int GCD, String fileFormat,String freelancerProfile) {

        this.GigId=gigId;
        this.fUsername = fUsername;
        GigTitle = gigTitle;
        this.GigDescription=gigDescription;
        Image1 = image1;
        this.price = price;
        this.GCD = GCD;
        this.GigFileFormat=fileFormat;
        this.freelancerProfile=freelancerProfile;
    }



    public GigModel(String gigTitle,String fUsername, String image1, int price, int GCD,String GigSubCategory,String freelancerProfile) {
        this.GigTitle = gigTitle;
        this.fUsername=fUsername;
        this.Image1 = image1;
        this.price = price;
        this.GCD = GCD;
        this.GigSubCategory=GigSubCategory;
        this.freelancerProfile=freelancerProfile;
    }

    ///used in Gigs.java

    public GigModel(int gigId,String gigTitle,String gigDescription,int price,int completionDays,String gigFileFormat
    ,String image1) {
        this.GigId=gigId;
        GigTitle = gigTitle;
        this.GigDescription=gigDescription;
        this.price=price;
        this.GCD=completionDays;
        this.GigFileFormat=gigFileFormat;
        this.Image1=image1;

    }

    public void setGigDescription(String gigDescription) {
        GigDescription = gigDescription;
    }

    public String getGigDescription() {
        return GigDescription;
    }

    public String getGigFileFormat() {
        return GigFileFormat;
    }

    public void setGigFileFormat(String gigFileFormat) {
        GigFileFormat = gigFileFormat;
    }

    public int getGigId() {
        return GigId;
    }

    public void setGigId(int gigId) {
        GigId = gigId;
    }

    public String getfUsername() {
        return fUsername;
    }

    public void setfUsername(String fUsername) {
        this.fUsername = fUsername;
    }

    public String getGigTitle() {
        return GigTitle;
    }

    public void setGigTitle(String gigTitle) {
        GigTitle = gigTitle;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getGCD() {
        return GCD;
    }

    public void setGCD(int GCD) {
        this.GCD = GCD;
    }
}
