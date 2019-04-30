package Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Market{
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@OneToMany
	private List<Service> closedService;
	
	@OneToMany
	private List<Service> inProgressService;
	
	@OneToMany
	private List<Service> openedService;

	public Market() {
		closedService = new ArrayList<>();
		openedService = new ArrayList<>();
		inProgressService = new ArrayList<>();
	}

	public Market(List<Service> closedService, List<Service> inProgressService, List<Service> openedService) {
		super();
		this.closedService = closedService;
		this.inProgressService = inProgressService;
		this.openedService = openedService;
	}

	public List<Service> getClosedService() {
		return closedService;
	}

	public void setClosedService(List<Service> closedService) {
		this.closedService = closedService;
	}

	public List<Service> getInProgressService() {
		return inProgressService;
	}

	public void setInProgressService(List<Service> inProgressService) {
		this.inProgressService = inProgressService;
	}

	public List<Service> getOpenedService() {
		return openedService;
	}

	public void setOpenedService(List<Service> openedService) {
		this.openedService = openedService;
	}
	
	@Override
	public String toString() {
		return "Market [closedService=" + closedService + ", inProgressService=" + inProgressService
				+ ", openedService=" + openedService + "]";
	}
	
}