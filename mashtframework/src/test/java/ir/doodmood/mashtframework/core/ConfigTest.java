package ir.doodmood.mashtframework.core;

import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Component;
import ir.doodmood.mashtframework.annotation.Properties;
import org.junit.Assert;
import org.junit.Test;

@Component
class Mamdali {
    String s1;
    @Autowired
    public Mamdali(@Properties("s1") String s1) {
        this.s1 = s1;
    }

    public String getS1() {
        return s1;
    }
}

@Component
class Mamdali2 {
    John s2;
    @Autowired
    public Mamdali2(@Properties("s2") John s2) {
        this.s2 = s2;
    }

    public John getS2() {
        return s2;
    }
}

class John {
    String username;
    String password;
    public String username() {
        return username;
    }
    public String password() {
        return password;
    }
}

@Component
class Mamdali3 {
    Mamdali mamdali;
    Mamdali2 mamdali2;
    Integer s3;

    @Autowired
    public Mamdali3(Mamdali mamdali, Mamdali2 mamdali2, @Properties("s3") Integer s3) {
        this.mamdali = mamdali;
        this.mamdali2 = mamdali2;
        this.s3 = s3;
    }

    public String getSun() {
        return mamdali.getS1() + mamdali2.getS2().username() + mamdali2.getS2().password() + s3;
    }
}

@Component
class Mamdali4 {
    public John john;
    @Autowired
    public Mamdali4(@Properties("s4") John john) {
        this.john = john;
    }
}

public class ConfigTest {
    @Test
    public void configSimple1Test() throws Exception {
        ComponentFactory cf = ComponentFactory.factory(Mamdali.class);
        Assert.assertEquals(((Mamdali) cf.getNew()).getS1(), "henlo");
    }

    @Test
    public void configSimple2Test() throws Exception {
        ComponentFactory cf = ComponentFactory.factory(Mamdali2.class);
        Assert.assertEquals(((Mamdali2) cf.getNew()).getS2().username(), "John");
        Assert.assertEquals(((Mamdali2) cf.getNew()).getS2().password(), "Doe");
    }

    @Test
    public void configHard1Test() throws Exception {
        ComponentFactory cf = ComponentFactory.factory(Mamdali3.class);
        Assert.assertEquals(((Mamdali3) cf.getNew()).getSun(), "henloJohnDoe12");
    }

    @Test
    public void configNotFoundTest() throws Exception {
        ComponentFactory cf = ComponentFactory.factory(Mamdali4.class);
        Mamdali4 mmd = (Mamdali4) cf.getNew();
        Assert.assertNull(mmd.john);
    }
}
