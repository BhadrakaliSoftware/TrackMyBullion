package com.riddhi.trackmybullion.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dataset {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("dataset_code")
    @Expose
    private String datasetCode;
    @SerializedName("database_code")
    @Expose
    private String databaseCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("refreshed_at")
    @Expose
    private String refreshedAt;
    @SerializedName("newest_available_date")
    @Expose
    private String newestAvailableDate;
    @SerializedName("oldest_available_date")
    @Expose
    private String oldestAvailableDate;
    @SerializedName("column_names")
    @Expose
    private List<String> columnNames = null;
    @SerializedName("frequency")
    @Expose
    private String frequency;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("premium")
    @Expose
    private Boolean premium;
    @SerializedName("limit")
    @Expose
    private Object limit;
    @SerializedName("transform")
    @Expose
    private Object transform;
    @SerializedName("column_index")
    @Expose
    private Object columnIndex;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("data")
    @Expose
    private List<List<String>> data = null;
    @SerializedName("collapse")
    @Expose
    private Object collapse;
    @SerializedName("order")
    @Expose
    private Object order;
    @SerializedName("database_id")
    @Expose
    private Integer databaseId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDatasetCode() {
        return datasetCode;
    }

    public void setDatasetCode(String datasetCode) {
        this.datasetCode = datasetCode;
    }

    public String getDatabaseCode() {
        return databaseCode;
    }

    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefreshedAt() {
        return refreshedAt;
    }

    public void setRefreshedAt(String refreshedAt) {
        this.refreshedAt = refreshedAt;
    }

    public String getNewestAvailableDate() {
        return newestAvailableDate;
    }

    public void setNewestAvailableDate(String newestAvailableDate) {
        this.newestAvailableDate = newestAvailableDate;
    }

    public String getOldestAvailableDate() {
        return oldestAvailableDate;
    }

    public void setOldestAvailableDate(String oldestAvailableDate) {
        this.oldestAvailableDate = oldestAvailableDate;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public Object getLimit() {
        return limit;
    }

    public void setLimit(Object limit) {
        this.limit = limit;
    }

    public Object getTransform() {
        return transform;
    }

    public void setTransform(Object transform) {
        this.transform = transform;
    }

    public Object getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Object columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }

    public Object getCollapse() {
        return collapse;
    }

    public void setCollapse(Object collapse) {
        this.collapse = collapse;
    }

    public Object getOrder() {
        return order;
    }

    public void setOrder(Object order) {
        this.order = order;
    }

    public Integer getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Integer databaseId) {
        this.databaseId = databaseId;
    }

}
