package Model;

import javax.persistence.Entity;

@Entity
public class Offer extends Service{

	private int nbPointMin;
	
	public Offer() {
		
	}

	public Offer(User userOffering, User userDemanding, String category, String nameObject, String availabilityDate, String availabilityTime,
			String localisation, String type, int nbPointMin) {
		super(userOffering, userDemanding, category, nameObject, availabilityDate, 
				availabilityTime, localisation, type);
		nbPointMin = nbPointMin;
	}

	public int getNbPointMin() {
		return nbPointMin;
	}

	public void setNbPointMin(int nbPointMin) {
		this.nbPointMin = nbPointMin;
	}
	
}