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
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<HelloRequest> sayHelloClientStreaming( StreamObserver<HelloReply> responseObserver) {
        HelloRequestStreamObserver streamObserverOfRequest = new HelloRequestStreamObserver(responseObserver);
        return streamObserverOfRequest;
        /*StreamObserver<HelloRequest> streamObserverOfRequest = new StreamObserver<HelloRequest>() {
            String names = "";
            @Override
            public void onNext(HelloRequest req) {
                names = names + " " + req.getName() + "! ";
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + names).build();
                responseObserver.onNext(reply);
                responseObserver.onCompleted();
            }
        };

        return streamObserverOfRequest;*/
    }

    @Override
    public void sayHelloServerStreaming(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        Thread worker = new ServerStreamingThread(request, responseObserver);
        worker.start();
        /*Thread worker = new Thread() {
            @Override
            public void run() {
                for( int i = 0; i < 10; i++) {
                    HelloReply reply = HelloReply.newBuilder().setMessage("Hello[" + i + "] " + request.getName()).build();
                    responseObserver.onNext(reply);
                }
                responseObserver.onCompleted();
            }
        };
        worker.start();*/
    }

    @Override
    public StreamObserver<HelloRequest> sayHelloBiStreaming( StreamObserver<HelloReply> responseObserver) {
        return null;
    }
}

class ServerStreamingThread extends Thread {
    private HelloRequest request; 
    private StreamObserver<HelloReply> responseObserver;

    public ServerStreamingThread(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        this.request = request;
        this.responseObserver = responseObserver;
    }

    @Override
    public void run() {
        for( int i = 0; i < 10; i++) {
            HelloReply reply = HelloReply.newBuilder().setMessage("Hello[" + i + "] " + request.getName()).build();
            responseObserver.onNext(reply);
        }
        responseObserver.onCompleted();
    }
}

class HelloRequestStreamObserver implements StreamObserver<HelloRequest> {
    private String names;
    private StreamObserver<HelloReply> responseObserver;

    public HelloRequestStreamObserver(StreamObserver<HelloReply> responseObserver) {
        this.names = "";
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(HelloRequest req) {
        names = names + " " + ((HelloRequest)req).getName() + "! ";
    }

    @Override
    public void onError(Throwable t) {
    }

    @Override
    public void onCompleted() {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + names).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
