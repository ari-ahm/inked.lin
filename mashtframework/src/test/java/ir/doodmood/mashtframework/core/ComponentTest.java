package ir.doodmood.mashtframework.core;

import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Component;
import ir.doodmood.mashtframework.exception.CircularDependencyException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

@Component
class Mammad {
    public Mammad() {
        System.out.println("hiiii from mammad");
    }

    public int getSix() {
        return 6;
    }
}

@Component
class Ali {
    private Ali() {
        System.out.println("hiiii from ali");
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

public class ComponentTest {
    @Test
    public void publicNoArgsConstructor() throws Throwable {
        ComponentFactory c = ComponentFactory.factory(Mammad.class);
        Mammad m1 = (Mammad)c.getNew();
        Assert.assertEquals(m1.getSix(), 6);
    }

    @Test
    public void privateNoArgsConstructor() throws Throwable {
        ComponentFactory c = ComponentFactory.factory(Ali.class);
        Ali a1 = (Ali)c.getNew();
        Assert.assertEquals(a1.getSeven(), 7);
    }

    @Test
    public void publicWithDependencyConstructor() throws Throwable {
        ComponentFactory c = ComponentFactory.factory(Jafar.class);
        Jafar j1 = (Jafar)c.getNew();
        Assert.assertEquals(j1.getMul(), 42);
    }

    @Test(expected = CircularDependencyException.class)
    public void circularDependencyTest() throws Throwable {
        ComponentFactory c = ComponentFactory.factory(Circ1.class);
    }
}
