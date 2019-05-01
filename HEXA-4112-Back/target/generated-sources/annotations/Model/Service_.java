package Model;

import Model.Person;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-30T19:29:08")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-01T08:55:18")
>>>>>>> 138fc3309b83e07840659179feb6a0472447d48f
@StaticMetamodel(Service.class)
public class Service_ { 

    public static volatile SingularAttribute<Service, Integer> duration;
    public static volatile SingularAttribute<Service, String> unit;
    public static volatile SingularAttribute<Service, Person> personOffering;
    public static volatile SingularAttribute<Service, String> localisation;
    public static volatile SingularAttribute<Service, Person> personDemanding;
    public static volatile SingularAttribute<Service, Date> availabilityDate;
    public static volatile SingularAttribute<Service, String> description;
    public static volatile SingularAttribute<Service, Long> id;
    public static volatile SingularAttribute<Service, String> category;
    public static volatile SingularAttribute<Service, String> type;
    public static volatile SingularAttribute<Service, String> nameObject;
    public static volatile SingularAttribute<Service, Date> availabilityTime;

}