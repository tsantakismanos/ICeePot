package iceepot.iceepotweb.sources;

import iceepot.iceepotweb.model.Date;
import iceepot.iceepotweb.model.Measurement;
import iceepot.iceepotweb.model.MeasurementType;

import java.util.List;

public interface Source {
		
	List<Measurement> listPotMeasurementByTypeDate(int potId, Date date, MeasurementType type) throws SourceException;
	
}
