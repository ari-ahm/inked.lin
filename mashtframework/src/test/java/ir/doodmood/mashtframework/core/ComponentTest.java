package ir.doodmood.mashtframework.core;

import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Component;
import ir.doodmood.mashtframework.exception.CircularDependencyException;
import ir.doodmood.mashtframework.exception.CriticalError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

@Component
class Mammad {
    public Mammad() {
//        System.out.println("hiiii from mammad");
    }

    public int getSix() {
        return 6;
    }
}

@Component
class Ali {
    private Ali() {
//        System.out.println("hiiii from ali");
    }

    public int getSeven() {
        return 7;
    }
}

@Component
class Jafar {
    private Mammad mammad;
    private Ali ali;

    @Autowired
    public Jafar(Mammad mmd, Ali ali) {
        this.mammad = mmd;
        this.ali = ali;
    }

    public int getMul() {
        return mammad.getSix() * ali.getSeven();
    }
}

@Component
class Circ1 {
    @Autowired
    public Circ1(Circ2 c) {}
}


@Component
class Circ2 {
    @Autowired
    public Circ2(Circ1 c) {}
}

@Component
class Sing {
    private static int t = 0;
    public Sing() {
        t++;
    }
    public int getT() {
        return t;
    }
}

@Component(singleton = true)
class Sing2 {
    private static int t = 0;
    public Sing2() {
        t++;
    }
    public int getT() {
        return t;
    }
}

public class ComponentTest {
    @Test
    public void publicNoArgsConstructor() throws Throwable {
        ComponentFactory c = ComponentFactory.factory(Mammad.class);
        Mammad m1 = (Mammad)c.getNew();
        Assertions.assertEquals(m1.getSix(), 6);
    }

    @Test
    public void privateNoArgsConstructor() throws Throwable {
        ComponentFactory c = ComponentFactory.factory(Ali.class);
        Ali a1 = (Ali)c.getNew();
        Assertions.assertEquals(a1.getSeven(), 7);
    }

    @Test
    public void publicWithDependencyConstructor() throws Throwable {
        ComponentFactory c = ComponentFactory.factory(Jafar.class);
        Jafar j1 = (Jafar)c.getNew();
        Assertions.assertEquals(j1.getMul(), 42);
    }

    @Test()
    public void circularDependencyTest() throws Throwable {
        Assertions.assertThrowsExactly(CriticalError.class, () -> {
            ComponentFactory c = ComponentFactory.factory(Circ1.class);
        });
    }

    @Test
    public void singletonTest() throws Throwable {
        ComponentFactory c = ComponentFactory.factory(Sing.class);
        Sing s1 = (Sing)c.getNew();
        Assertions.assertEquals(s1.getT(), 1);
        s1 = (Sing)c.getNew();
        Assertions.assertEquals(s1.getT(), 2);
        ComponentFactory.setSingleton(Sing.class, true);
        s1 = (Sing)c.getNew();
        Assertions.assertEquals(s1.getT(), 3);
        s1 = (Sing)c.getNew();
        Assertions.assertEquals(s1.getT(), 3);
        s1 = (Sing)c.getNew();
        Assertions.assertEquals(s1.getT(), 3);
    }

    @Test
    public void singleton2Test() throws Throwable {
        ComponentFactory c = ComponentFactory.factory(Sing2.class);
        Sing2 s1 = (Sing2)c.getNew();
        Assertions.assertEquals(s1.getT(), 1);
        s1 = (Sing2)c.getNew();
        Assertions.assertEquals(s1.getT(), 1);
        s1 = (Sing2)c.getNew();
        Assertions.assertEquals(s1.getT(), 1);
        s1 = (Sing2)c.getNew();
        Assertions.assertEquals(s1.getT(), 1);
        s1 = (Sing2)c.getNew();
        Assertions.assertEquals(s1.getT(), 1);
    }
}
