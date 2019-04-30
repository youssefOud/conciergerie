package Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public class Service{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	
	protected User userOffering;
	
	protected User userDemanding;
	
	protected String category;
	
	protected String nameObject;
	
	protected String availabilityDate;
	
	protected String availabilityTime;
	
	protected String localisation;
	
	protected String type;
	
	public Service() {
		
	}
	
	public Service(User userOffering, User userDemanding, String category, String nameObject, String availabilityDate,
			String availabilityTime, String localisation, String type) {
		this.userOffering = userOffering;
		this.userDemanding = userDemanding;
		this.category = category;
		this.nameObject = nameObject;
		this.availabilityDate = availabilityDate;
		this.availabilityTime = availabilityTime;
		this.localisation = localisation;
		this.type = type;
	}

	public User getUserOffering() {
		return userOffering;
	}

	public void setUserOffering(User userOffering) {
		this.userOffering = userOffering;
	}

	public User getUserDemanding() {
		return userDemanding;
	}

	public void setUserDemanding(User userDemanding) {
		this.userDemanding = userDemanding;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getNameObject() {
		return nameObject;
	}

	public void setNameObject(String nameObject) {
		this.nameObject = nameObject;
	}

	public String getAvailabilityDate() {
		return availabilityDate;
	}

	public void setAvailabilityDate(String availabilityDate) {
		this.availabilityDate = availabilityDate;
	}

	public String getAvailabilityTime() {
		return availabilityTime;
	}

	public void setAvailabilityTime(String availabilityTime) {
		this.availabilityTime = availabilityTime;
	}

	public String getLocalisation() {
		return localisation;
	}

	public void setLocalisation(String localisation) {
		this.localisation = localisation;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String serviceString = "Service : " + userOffering + " propose un " + nameObject;
		return serviceString;
	}
	
}