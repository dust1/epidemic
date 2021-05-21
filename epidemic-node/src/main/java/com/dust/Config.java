package com.dust;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Objects;
import java.util.Properties;

abstract public class Config {

    @Getter
    public enum Parameter {
        //常规配置参数
        ROUTER_PATH("router_path", null, String.class, true),
        STORAGE_PATH("storage_path", null, String.class, true),
        NODE_PORT("node_port", null, Integer.class, true),
        BUCKET_KEY("bucket_key", 20, Integer.class, false),
        NODE_SALT("node_salt", "", String.class, false),
        CHUNK_SIZE("chunk_size", "64MB", String.class, false),
        LAYOUT_SAVE_MAX_SIZE("layout_save_max_size", 10, Integer.class, false),
        LAYOUT_SAVE_MAX_TIME("layout_save_max_time", 10, Integer.class, false),
        LAYOUT_START_PING_COUNT("start_ping_count", 18, Integer.class, false),
        ORDER_NUM("order_num", 101, Integer.class, false);

        /**
         * .property文件中的字符串表达形式，即key
         */
        private final String     propertyString;

        /**
         * 参数的类别，用于反序列化。
         */
        private final Class<?>      propertyClass;

        /**
         * 如果属性文件和对象中都没有参数，则使用默认参数
         */
        private final Object     defaultValue;

        /**
         * 该参数是否为必填。true为必填
         */
        private final Boolean    required;

        Parameter(String propString, Object defaultValue, Class<?> propClass, Boolean req) {
            this.propertyString = propString;
            this.defaultValue = defaultValue;
            this.propertyClass = propClass;
            this.required = req;
        }
    }

    /**
     * 配置参数哈希表
     */
    protected EnumMap<Parameter, Object> parameter = new EnumMap<>(Parameter.class);

    protected final Properties props;

    public Config(Properties prop) {
        this.props = new Properties(prop);
    }

    /**
     * 从配置文件中读取配置参数
     * @param parameter 要读取的配置参数名称
     * @return 如果设置了参数，则返回值的对象。 否则返回null。
     */
    protected Object readParameter(Parameter parameter) {
        String tmpString = props.getProperty(parameter.getPropertyString());
        if ((Objects.isNull(tmpString) || tmpString.isBlank()) && parameter.getRequired()) {
            throw new NullPointerException("配置参数为空，请填写:" + parameter.getPropertyString());
        }
        if (Objects.isNull(tmpString) || tmpString.isBlank()) {
            tmpString = parameter.getDefaultValue().toString();
        }
        if (Integer.class.equals(parameter.getPropertyClass())) {
            return Integer.parseInt(tmpString.trim());
        } else if (String.class.equals(parameter.getPropertyClass())) {
            return tmpString.trim();
        }

        return null;
    }

}
