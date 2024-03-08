package com.backend.remindmedapi.apiCalls

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Meta(
    val disclaimer: String,
    val terms: String,
    val license: String,
    @SerialName("last_updated")
    val lastUpdated: String,
    val results: Results
)

@Serializable
data class Results(
    val skip: Int,
    val limit: Int,
    val total: Int
)

@Serializable
data class SingleDrugRes(
    val meta: Meta,
    val results: List<DrugResult>
)

@Serializable
data class DrugResult(
    @SerialName("spl_product_data_elements")
    val splProductDataElements: List<String>,
    @SerialName("spl_unclassified_section")
    val splUnclassifiedSection: List<String>,
    @SerialName("active_ingredient")
    val activeIngredient: List<String>,
    val purpose: List<String>,
    @SerialName("indications_and_usage")
    val indicationsAndUsage: List<String>,
    val warnings: List<String>,
    @SerialName("do_not_use")
    val doNotUse: List<String>,
    @SerialName("ask_doctor")
    val askDoctor: List<String>,
    @SerialName("ask_doctor_or_pharmacist")
    val askDoctorOrPharmacist: List<String>,
    @SerialName("when_using")
    val whenUsing: List<String>,
    @SerialName("stop_use")
    val stopUse: List<String>,
    @SerialName("pregnancy_or_breast_feeding")
    val pregnancyOrBreastFeeding: List<String>,
    @SerialName("keep_out_of_reach_of_children")
    val keepOutOfReachOfChildren: List<String>,
    @SerialName("dosage_and_administration")
    val dosageAndAdministration: List<String>,
    @SerialName("dosage_and_administration_table")
    val dosageAndAdministrationTable: List<String>,
    @SerialName("inactive_ingredient")
    val inactiveIngredient: List<String>,
    @SerialName("recent_major_changes")
    val recentMajorChanges: List<String>,
    @SerialName("package_label_principal_display_panel")
    val packageLabelPrincipalDisplayPanel: List<String>,
    @SerialName("set_id")
    val setId: String,
    val id: String,
    @SerialName("effective_time")
    val effectiveTime: String,
    val version: String,
    val openfda: OpenFDA
)

@Serializable
data class OpenFDA(
    @SerialName("application_number")
    val applicationNumber: List<String>,
    @SerialName("brand_name")
    val brandName: List<String>,
    @SerialName("generic_name")
    val genericName: List<String>,
    @SerialName("manufacturer_name")
    val manufacturerName: List<String>,
    @SerialName("product_ndc")
    val productNdc: List<String>,
    @SerialName("product_type")
    val productType: List<String>,
    val route: List<String>,
    @SerialName("substance_name")
    val substanceName: List<String>,
    val rxcui: List<String>,
    @SerialName("spl_id")
    val splId: List<String>,
    @SerialName("spl_set_id")
    val splSetId: List<String>,
    @SerialName("package_ndc")
    val packageNdc: List<String>,
    @SerialName("is_original_packager")
    val isOriginalPackager: List<Boolean>,
    val nui: List<String>,
    @SerialName("pharm_class_moa")
    val pharmClassMoa: List<String>,
    @SerialName("pharm_class_cs")
    val pharmClassCs: List<String>,
    @SerialName("pharm_class_epc")
    val pharmClassEpc: List<String>,
    val unii: List<String>
)
