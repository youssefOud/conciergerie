package Model;

import javax.persistence.Entity;

@Entity
public class Demand extends Service {

	// TODO : Verifier float ou pas
	private int nbPointMax;
	
	public Demand() {
		
	}
	
	public Demand(User userOffering, User userDemanding, String category, String nameObject, String availabilityDate, String availabilityTime,
			String localisation, String type) {
		// TODO A implementer
		super(userOffering, userDemanding, category, nameObject, availabilityDate, 
				availabilityTime, localisation, type);
		nbPointMax = nbPointMax;
	}

	public int getNbPointMax() {
		return nbPointMax;
	}

	public void setNbPointMax(int nbPointMax) {
		this.nbPointMax = nbPointMax;
	}
}