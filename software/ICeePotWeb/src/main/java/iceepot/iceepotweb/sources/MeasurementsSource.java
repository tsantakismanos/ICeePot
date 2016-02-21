package iceepot.iceepotweb.sources;

import iceepot.iceepotweb.model.Measurement;
import iceepot.iceepotweb.model.MeasurementType;

import java.util.List;

public interface MeasurementsSource {
	
	List<Measurement> getByMonthNYear(int month, int year, int potId, MeasurementType type) throws Exception;
	List<Measurement> getByRange(int monthFrom, int yearFrom, int monthTo, int yearTo, int potId, MeasurementType type) throws Exception;
	
}
