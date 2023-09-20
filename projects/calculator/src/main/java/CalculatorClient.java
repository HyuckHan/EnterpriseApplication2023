import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import calculator.*;

public class CalculatorClient {
    private static CalculatorGrpc.CalculatorBlockingStub blockingStub;

    public static void main(String[] args) throws Exception {
        String target = "localhost:50051";
        
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();
        try {
            blockingStub = CalculatorGrpc.newBlockingStub(channel);
            Operands operands = Operands.newBuilder().setOp1(6).setOp2(3).build();
            Result result = null;
            try {
                result = blockingStub.add(operands);
                System.out.println("6 + 3 = " + result.getValue());
                result = blockingStub.subtract(operands);
                System.out.println("6 - 3 = " + result.getValue());
                result = blockingStub.multiply(operands);
                System.out.println("6 * 3 = " + result.getValue());
            } catch (StatusRuntimeException e) {
                System.out.println("WARNING RPC failed: " + e.getStatus());
                return;
            }
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
