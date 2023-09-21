import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import helloworld.*;

public class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
        Thread worker = new HelloWorker(req, responseObserver);
        worker.start();
    }
}

class HelloWorker extends Thread {
    private HelloRequest request;
    private StreamObserver<HelloReply> responseObserver;

    public HelloWorker(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        this.request = request;
        this.responseObserver = responseObserver;
    }

    @Override
    public void run() {
        try {
            Thread.currentThread().sleep(5000);
        } catch(Exception e) {
            e.printStackTrace();
        }
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
