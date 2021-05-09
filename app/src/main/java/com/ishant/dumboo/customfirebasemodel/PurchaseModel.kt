package com.ishant.dumboo.customfirebasemodel

class PurchaseModel {
    var Plan:String=""
    var PlanId:Int=0
    var PlanPurchaceDate:Long=0
    var PlanNextDueDate:Long=0

    constructor(Plan: String, PlanId: Int, PlanPurchaceDate: Long, PlanNextDueDate: Long) {
        this.Plan = Plan
        this.PlanId = PlanId
        this.PlanPurchaceDate = PlanPurchaceDate
        this.PlanNextDueDate = PlanNextDueDate
    }

    constructor()

}