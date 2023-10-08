import io.grpc.NameResolverProvider;
import io.grpc.NameResolver;
import io.grpc.Attributes;

import java.net.URI;

public class ZkNameResolverProvider extends NameResolverProvider {
    @Override
    public NameResolver newNameResolver(URI targetUri, NameResolver.Args args) {
        return new ZkNameResolver(targetUri);
    }

    @Override
    protected int priority() {
        return 5;
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    public String getDefaultScheme() {
        return "zk";
    }
}

