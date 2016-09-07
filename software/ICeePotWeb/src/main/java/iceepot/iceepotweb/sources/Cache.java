package iceepot.iceepotweb.sources;

import iceepot.iceepotweb.model.Date;
import iceepot.iceepotweb.model.Measurement;
import iceepot.iceepotweb.model.MeasurementType;

import java.util.HashMap;
import java.util.List;

public class Cache  {

	
	HashMap<Key, List<Measurement>> cache = new HashMap<Key, List<Measurement>>();
	
	
	public Cache() {
		this.cache = new HashMap<Cache.Key, List<Measurement>>();
	}


	public Cache(HashMap<Key, List<Measurement>> cache) {
	
		this.cache = cache;
	}

	
	public List<Measurement> find(int potId, Date date,
			MeasurementType type) throws SourceException {
		
		return cache.get(new Key(potId, date, type));
	}
	
	public void store(int potId, Date date,
			MeasurementType type, List<Measurement> measurements) throws SourceException {
		
		cache.put(new Key(potId, date, type), measurements);
	}



	private class Key{
		

		private Integer potId;
		private Date date;
		private MeasurementType type;
					
				
		public Key(Integer potId, Date date, MeasurementType type) {
			super();
			this.potId = potId;
			this.date = date;
			this.type = type;
		}
		
		

		public Integer getPotId() {
			return potId;
		}



		public Date getDate() {
			return date;
		}

		
		public MeasurementType getType() {
			return type;
		}


		@Override
		public int hashCode() {
			
			return potId + date.getMonth() + date.getYear() + type.ordinal();
		}
		
		@Override
		public boolean equals(Object obj) {
			
			
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if(this.getPotId().compareTo(other.getPotId()) != 0)
				return false;
			if(this.getDate().getMonth() != other.getDate().getMonth())
				return false;
			if(this.getDate().getYear() != other.getDate().getYear())
				return false;
			if(this.getType() != other.getType())
				return false;

			return true;
			

			
		}
	}

}
