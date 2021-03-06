package vinscom.ioc.test;

import io.vertx.core.json.JsonObject;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.Logger;

import org.junit.Test;

import vinscom.ioc.Glue;
import vinscom.ioc.component.Initial;
import vinscom.ioc.common.TestConstant;
import vinscom.ioc.test.component.EnumTestValues;
import vinscom.ioc.test.component.PropertiesComponent;

public class FullConfigurationTest {

  @Test
  public void loadGlobalComonentSetInPropertyFile() {
    Object inst = Glue.instance().<Object>resolve("/vinscom/ioc/test/component/GlobalObjectSetSpecifically", Object.class);
    assertNotNull("Load global scope component when set in property file", inst);
    assertEquals(inst.getClass(), Object.class);

    Object inst2 = Glue.instance().<Object>resolve("/vinscom/ioc/test/component/GlobalObjectSetSpecifically", Object.class);
    assertSame(inst, inst2);
  }

  @Test
  public void loadGlobalComonentNotSetInPropertyFile() {
    Object inst = Glue.instance().<Object>resolve("/vinscom/ioc/test/component/GlobalObjectByDefault", Object.class);
    assertNotNull("Load global scope component when set in property file", inst);
    assertEquals(inst.getClass(), Object.class);

    Object inst2 = Glue.instance().<Object>resolve("/vinscom/ioc/test/component/GlobalObjectByDefault", Object.class);
    assertSame(inst, inst2);
  }

  @Test
  public void loadLocalScopeComponent() {
    Object inst = Glue.instance().<Object>resolve("/vinscom/ioc/test/component/LocalObject", Object.class);
    assertNotNull("Load local scope component when set in property file", inst);
    assertEquals(inst.getClass(), Object.class);

    Object inst2 = Glue.instance().<Object>resolve("/vinscom/ioc/test/component/LocalObject", Object.class);
    assertNotSame(inst, inst2);
  }

  @Test
  public void stringProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    assertEquals("TestString", inst.getPropString());
    inst.setPropString("Wrong String");
    assertEquals("Wrong String", inst.getPropString());
    inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    assertEquals("TestString", inst.getPropString());
  }

  @Test
  public void booleanProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    assertTrue(inst.isPropBoolean());
    assertTrue(inst.isPropBoolean2());
  }

  @Test
  public void longProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    assertEquals(inst.getPropLong(), 2l);
    assertTrue(inst.getPropLong2().equals(2l));
  }

  @Test
  public void fileProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    assertEquals(inst.getPropFile().getName(), "testconfig.json");
  }

  @Test
  public void stringArrayProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    assertArrayEquals(new String[]{"a", "b", "c"}, inst.getPropArray());
    inst.setPropArray(new String[]{});
    assertArrayEquals(new String[]{}, inst.getPropArray());
    inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    assertArrayEquals(new String[]{"a", "b", "c"}, inst.getPropArray());
  }

  @Test
  public void stringListProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    List<String> expected = new ArrayList<>();
    expected.add("a");
    expected.add("b");
    expected.add("c");
    expected.removeAll(inst.getPropList());
    assertEquals(expected.size(), 0);
  }

  @Test
  public void stringSetProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    Set<String> expected = new HashSet<>();
    expected.add("a");
    expected.add("b");
    expected.add("c");
    expected.removeAll(inst.getPropSet());
    assertEquals(expected.size(), 0);
  }

  @Test
  public void mapProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    Map<String, String> result = inst.getPropMap();
    assertEquals("b", result.remove("a"));
    assertEquals("d", result.remove("c"));
    assertEquals("f", result.remove("e"));
  }

  @Test
  public void integerProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    assertEquals(inst.getPropInt(), 2);
    assertTrue(inst.getPropInteger().equals(2));
  }

  @Test
  public void loggerAsProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    Logger logger = inst.getPropLogger();
    assertEquals(logger.getName(), "vinscom.ioc.test.component.PropertiesComponent");
  }

  @Test
  public void anotherComponentProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    assertSame(Object.class, inst.getPropComponent().getClass());
  }

  @Test
  public void mergeProperties() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/MergedComponent", PropertiesComponent.class);
    assertEquals("TestString2", inst.getPropString());
    assertArrayEquals(new String[]{"a"}, inst.getPropArray());
    assertEquals(5, inst.getPropList().size());
    assertEquals(3, inst.getPropMap().size());
    assertEquals(3, inst.getPropSet().size());

    PropertiesComponent refInst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent2", PropertiesComponent.class);
    assertEquals(refInst.getPropString(), "TestString3");
  }

  @Test
  public void componentStartUp() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    assertTrue(inst.isStartup());
  }

  @Test
  public void jsonProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/MergedComponent", PropertiesComponent.class);
    assertEquals(new JsonObject(TestConstant.TEST_JSON).toString(), inst.getPropJson().toString());
  }

  @Test
  public void enumProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    assertEquals(EnumTestValues.TWO, inst.getPropEnum());
  }

  @Test
  public void serviceMapProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    Object inst2 = Glue.instance().<Object>resolve("/vinscom/ioc/test/component/GlobalObjectByDefault", Object.class);
    assertEquals(3, inst.getPropServiceMap().getServices().size());
    assertSame(inst2, inst.getPropServiceMap().get("a"));
    assertSame(inst2, inst.getPropServiceMap().get("b"));
    assertSame(inst2, inst.getPropServiceMap().get("c"));
  }

  @Test
  public void serviceArrayProperty() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    Object inst2 = Glue.instance().<Object>resolve("/vinscom/ioc/test/component/GlobalObjectByDefault", Object.class);
    assertEquals(3, inst.getPropServiceArray().getServices().size());
    assertSame(inst2, inst.getPropServiceArray().get(0));
    assertSame(inst2, inst.getPropServiceArray().get(1));
    assertSame(inst2, inst.getPropServiceArray().get(2));
  }

  @Test
  public void componentInitial() {
    Initial inst = Glue.instance().<Initial>resolve("/vinscom/ioc/test/component/Initial", Initial.class);
    List<Object> comps = inst.getComponents();
    assertEquals(comps.size(), 4);
    comps.forEach((comp) -> {
      assertEquals(comp.getClass(), PropertiesComponent.class);
    });
  }

  @Test
  public void refComponentTest() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
    PropertiesComponent inst2 = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/RefPropertiesComponent", PropertiesComponent.class);
    PropertiesComponent inst3 = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/MergedComponent", PropertiesComponent.class);
    Object inst4 = Glue.instance().<Object>resolve("/vinscom/ioc/test/component/GlobalObjectByDefault", Object.class);
    assertEquals(inst.getPropString(), inst2.getPropString());
    assertArrayEquals(inst.getPropArray(), inst2.getPropArray());
    assertTrue(inst.getPropList().equals(inst2.getPropList()));
    assertTrue(inst.getPropMap().keySet().equals(inst2.getPropMap().keySet()));
    assertSame(inst.getPropComponent(), inst2.getPropComponent());
    assertEquals(inst.isPropBoolean(), inst2.isPropBoolean());
    assertEquals(inst.getPropEnum(), inst2.getPropEnum());
    assertEquals(inst3.getPropJson(), inst2.getPropJson());

    assertEquals(3, inst2.getPropServiceMap().getServices().size());
    assertSame(inst4, inst2.getPropServiceMap().get("a"));
    assertSame(inst4, inst2.getPropServiceMap().get("b"));
    assertSame(inst4, inst2.getPropServiceMap().get("c"));
  }
  
  @Test
  public void basedOnComponentTest() {
    PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/BasedOnPropertiesComponent", PropertiesComponent.class);
    assertEquals("TestString2",inst.getPropString());
  }
  
   @Test
   public void nullProperty(){
     PropertiesComponent inst = Glue.instance().<PropertiesComponent>resolve("/vinscom/ioc/test/component/PropertiesComponent", PropertiesComponent.class);
     assertNull(inst.getPropNullString());
     assertNull(inst.getPropNullServiceMap());
     assertNull(inst.getPropNullComponent());
   }
}
