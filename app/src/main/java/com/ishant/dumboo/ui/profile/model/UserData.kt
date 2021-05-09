package com.ishant.dumboo.ui.profile.model

class UserData {


    var Name: String = ""
    var Gender: String = ""
    var Email: String = ""
    var UserId: String = ""
    var Mobile: String = ""
    var LastUpdatedProfile: Long = 0
    var Profile: String = ""



    constructor() {

    }

    constructor(
        Name: String,
        Gender: String,
        Email: String,
        UserId: String,
        LastUpdatedProfile: Long,
        Profile: String,
        Mobile:String
    ) {
        this.Name = Name
        this.Gender = Gender
        this.Email = Email
        this.UserId = UserId
        this.LastUpdatedProfile = LastUpdatedProfile
        this.Profile = Profile
        this.Mobile=Mobile

    }

}