package com.storyteller.gui.create_html;

import java.lang.reflect.Field;

public class RegularType extends HTMLFormItem {
    /**
     * 0 无验证 如果使用了谛听，也是0参数
     * 1 系统验证 与bootstrap 的区别是不添加相关类，和相关监听事件
     */
    private int verifyType;

    public RegularType(int verifyType) {
        super();
        this.verifyType = verifyType;
    }

    @Override
    public String getInput(String name, String type, Field field) {
        return String.format("<input type='%s' id='%s' name='%s' %s>", type, name, name, needVerify() ? "{verify}" : "");
    }

    @Override
    public String getLabel(String name, String realName) {
        return String.format("<label for='%s'>" +
                "%s" +
                "</label>", name, realName);
    }

    @Override
    public String getContainer() {
        return "<div>\n" +
                "\t{name}\n" +
                "\t{label}\n" +
                "\t{input}\n" +
                "</div>";
    }

    @Override
    public String getSelect(String name, String realName, Field field) {
        return String.format(
                "<select name='%s' id='%s' %s>\r\n" +
                        "{option}" +
                        "</select>", name, name, needVerify() ? "{verify}" : "");
    }

    @Override
    public String parse(String name, String realName, Field field) throws Exception {
        String replace = getContainer().replace("{label}", getLabel(name, realName));
        String result;
        if (isSelector(field)) {
            result = replace.replace("{input}", getSelect(name, realName, field));
        } else
            result = replace.replace("{input}", getInput(name, getInputType(field), field));
        return result.replace("{verify}", needVerify() ? getVerification(field) : "");
    }

    @Override
    public boolean needVerify() {
        if (verifyType == 1) {
            return true;
        }
        return false;
    }

}
