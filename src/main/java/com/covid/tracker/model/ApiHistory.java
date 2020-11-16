package com.covid.tracker.model;

import java.util.Date;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "apihistory")
public class ApiHistory {

	@Id
	private ObjectId _id;

	@Indexed
	private String apiName;

	private String type;

	private Date date;

	public ApiHistory() {
		super();
	}

	public ApiHistory(String _id, String apiName, Date date, String type) {
		super();
		this._id = (null == _id) ? new ObjectId() : new ObjectId(_id);
		this.apiName = apiName;
		this.date = date;
		this.type = type;
	}

	public ApiHistory(ObjectId _id, String apiName, Date date, String type) {
		super();
		this._id = (null == _id) ? new ObjectId() : _id;
		this.apiName = apiName;
		this.date = date;
		this.type = type;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_id, apiName, date, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApiHistory other = (ApiHistory) obj;
		return Objects.equals(_id, other._id) && Objects.equals(apiName, other.apiName)
				&& Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "ApiHistory [_id=" + _id + ", apiName=" + apiName + ", type=" + type + ", date=" + date + "]";
	}

}
