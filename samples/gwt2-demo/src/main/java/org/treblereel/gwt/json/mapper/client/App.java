/*
 * Copyright © 2022 Treblereel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.treblereel.gwt.json.mapper.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import com.google.gwt.user.client.ui.VerticalPanel;
import elemental2.dom.DomGlobal;

public class App implements EntryPoint {

    @Override
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

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setHeight("4000");
        verticalPanel.setWidth("4000");

        String json = mapper.toJSON(bean);
        verticalPanel.getElement().setInnerHTML(json);

        String json2 = "{\"name\":\"name\",\"value\":\"value\", \"unknown\":\"unknown\", \"beanTwo\":{\"name\":\"simple_name\",\"age\":8888,\"address\":\"qwerty_asdfgh\",\"beanThree\":{\"id\":\"ID\"}}}";

        BeanOne bean2 = mapper.fromJSON(json2);

        DomGlobal.console.log("bean2: " + bean2);

        RootPanel.get().add(verticalPanel);
    }
}
