package edu.minggo.tencent.weibo;

import java.io.IOException;
import java.io.Serializable;


import org.apache.commons.net.util.Base64;

public abstract class OAuthMessageSigner implements Serializable {

    private static final long serialVersionUID = 4445779788786131202L;

    private transient Base64 base64;

    private String consumerSecret;

    private String tokenSecret;

    public OAuthMessageSigner() {
        this.base64 = new Base64();
    }

    public abstract String sign(HttpRequest request, HttpParameters requestParameters)
            throws OAuthMessageSignerException;

    public abstract String getSignatureMethod();

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    protected byte[] decodeBase64(String s) {
        return base64.decode(s.getBytes());
    }

    protected String base64Encode(byte[] b) {
        return new String(base64.encode(b));
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.base64 = new Base64();
    }
}
