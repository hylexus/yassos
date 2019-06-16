package io.github.hylexus.yassos.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-06-16 19:54
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Resp<T> {
    public static final int SUCCESS = 0;
    public static final int SERVER_ERROR = 0;
    private int code;
    private String msg;
    private T body;

    private Resp() {
    }

    public static <E> Resp<E> success(String msg, E body) {
        return success(SUCCESS, msg, body);
    }

    public static <E> Resp<E> success(E body) {
        return success(SUCCESS, null, body);
    }

    public static <E> Resp<E> success() {
        return success(SUCCESS, null, null);
    }

    public static <E> Resp<E> success(int code, String msg, E body) {
        return new Resp<E>().setCode(code).setMsg(msg).setBody(body);
    }

    public static <E> Resp<E> failure(String msg) {
        return failure(msg, null);
    }

    public static <E> Resp<E> failure(String msg, E body) {
        return failure(SERVER_ERROR, msg, body);
    }

    public static <E> Resp<E> failure(int code, String msg, E body) {
        return new Resp<E>().setCode(code).setMsg(msg).setBody(body);
    }
}
