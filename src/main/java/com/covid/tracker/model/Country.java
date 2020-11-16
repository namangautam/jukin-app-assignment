package com.covid.tracker.model;

import java.util.Objects;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.covid.tracker.repsitory.CountryRepository;

@Document(collection = "country")
public class Country {

	@Id
	private ObjectId _id;
	private String name;
	private String alpha2code;
	private String alpha3code;
	private double latitude;
	private double longitude;
	private boolean favourite;

	public Country() {
		super();
	}

	public Country(String _id, String name, String alpha2code, String alpha3code, double latitude, double longitude) {
		super();
		this.name = name;
		this.alpha2code = alpha2code;
		this.alpha3code = alpha3code;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Country(String _id, String name, String alpha2code, String alpha3code, double latitude, double longitude,
			boolean isFavourite) {
		super();
		this.name = name;
		this.alpha2code = alpha2code;
		this.alpha3code = alpha3code;
		this.latitude = latitude;
		this.longitude = longitude;
		this.favourite = isFavourite;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlpha2code() {
		return alpha2code;
	}

	public void setAlpha2code(String alpha2code) {
		this.alpha2code = alpha2code;
	}

	public String getAlpha3code() {
		return alpha3code;
	}

	public void setAlpha3code(String alpha3code) {
		this.alpha3code = alpha3code;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean isFavourite() {
		return favourite;
	}

	public void setFavourite(boolean isFavourite) {
		this.favourite = isFavourite;
	}

	@Override
	public int hashCode() {
		return Objects.hash(alpha2code, alpha3code, latitude, longitude, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		return Objects.equals(alpha2code, other.alpha2code) && Objects.equals(alpha3code, other.alpha3code)
				&& Double.doubleToLongBits(latitude) == Double.doubleToLongBits(other.latitude)
				&& Double.doubleToLongBits(longitude) == Double.doubleToLongBits(other.longitude)
				&& Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Country [name=" + name + ", alpha2code=" + alpha2code + ", alpha3code=" + alpha3code + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}
}
