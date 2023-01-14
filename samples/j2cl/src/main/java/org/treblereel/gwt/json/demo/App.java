package org.treblereel.gwt.json.demo;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;


public class App {

    public void onModuleLoad() {

        BeanOne_JsonMapperImpl mapper = BeanOne_JsonMapperImpl.INSTANCE;

        BeanOne bean = new BeanOne();

        bean.setName("name");
        bean.setValue("value");

        BeanTwo two = new BeanTwo();
        bean.setBeanTwo(two);

        two.setName("simple_name");
        two.setAge(8888);
        two.setAddress("qwerty_asdfgh");

        BeanThree three = new BeanThree();
        three.setId("ID");
        two.setBeanThree(three);

        HTMLDivElement verticalPanel = (HTMLDivElement) DomGlobal.document.createElement("div");
        String json = mapper.toJSON(bean);
        verticalPanel.innerHTML = (json);

        String json2 = "{\"name\":\"name\",\"value\":\"value\", \"unknown\":\"unknown\", \"beanTwo\":{\"name\":\"simple_name\",\"age\":8888,\"address\":\"qwerty_asdfgh\",\"beanThree\":{\"id\":\"ID\"}}}";

        BeanOne bean2 = mapper.fromJSON(json2);

        DomGlobal.console.log("bean2: " + bean2);

        DomGlobal.document.body.appendChild(verticalPanel);
    }
}
