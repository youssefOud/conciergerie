package Model;

import Model.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-01T08:55:18")
@StaticMetamodel(Service.class)
public class Service_ { 

    public static volatile SingularAttribute<Service, User> userDemanding;
    public static volatile SingularAttribute<Service, String> localisation;
    public static volatile SingularAttribute<Service, String> availabilityDate;
    public static volatile SingularAttribute<Service, User> userOffering;
    public static volatile SingularAttribute<Service, Long> id;
    public static volatile SingularAttribute<Service, String> category;
    public static volatile SingularAttribute<Service, String> type;
    public static volatile SingularAttribute<Service, String> nameObject;
    public static volatile SingularAttribute<Service, String> availabilityTime;

}