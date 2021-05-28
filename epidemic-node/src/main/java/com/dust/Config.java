package com.dust;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Objects;
import java.util.Properties;

abstract public class Config {

    @Getter
    public enum Parameter {
        //常规配置参数

        //路由信息存储路径
        ROUTER_PATH("router_path", null, String.class, true),
        //数据存储路径
        STORAGE_PATH("storage_path", null, String.class, true),
        //本节点监听端口
        NODE_PORT("node_port", null, Integer.class, true),
        //节点路由表bucket的大小
        BUCKET_KEY("bucket_key", 20, Integer.class, false),
        //节点id生成时的影响因素
        NODE_SALT("node_salt", "", String.class, false),
        //存储数据块大小
        CHUNK_SIZE("chunk_size", "64MB", String.class, false),
        //发起一次Re-Publishing的时间间隔,默认1小时，最低也是1小时
        RE_PUBLISHING_TIME("re_publishing_time", "1h", String.class, true),
        //发起Re-Publishing时的线程池大小
        RE_PUBLISHING_THREAD_POOL_SIZE("re_publishing_thread_pool_size", 10, Integer.class, true),
        //触发save操作的节点数量
        ROUTER_SAVE_COUNT("router_save_count", 20, Integer.class, false),
        //文件目录树每层的数量
        ORDER_NUM("order_num", 101, Integer.class, false),
        //联络节点host。最重要的参数，必须依靠该节点才能加入集群中
        CONTACT_HOST("contact_host", null, String.class, true),
        //联络节点的port，最重要的参数，必须依靠该节点才能加入集群中
        CONTACT_PORT("contact_port", null, Integer.class, true);

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
