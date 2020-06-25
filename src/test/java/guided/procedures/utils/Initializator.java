package guided.procedures.utils;

import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import guided.procedures.SessionAttributes;
import guided.procedures.model.ProcedureManager;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Optional;

public class Initializator {
    @Getter
    private ProcedureManager procedureManager;
    private String fileName;
    private String procedureName;
    @Getter
    private RequestHandler handler;
    private Request request;
    @SneakyThrows

    private Initializator(Builder builder){
        SessionAttributes.overrideResourceFile(builder.fileName);
        SessionAttributes.initiateAttributes();
        if(builder.procedureManager != null){
            SessionAttributes.setProcedureManager(builder.procedureManager);
            this.procedureManager = builder.procedureManager;
            this.procedureManager.withProcedure(builder.procedureName);
        }
        this.request = builder.request;
        this.fileName = builder.fileName;
        this.procedureName = builder.procedureName;
        this.handler = builder.handler;
    }

    public static Builder builder(){
        return new Builder();
    }

    public Optional<Response> getUpdatedResponse(){
        return handler.handle(HandlerMockUtil.getHandlerWithRequest(this.request));
    }

    public static class Builder{
        private String fileName;
        private ProcedureManager procedureManager;
        private String procedureName;
        private RequestHandler handler;
        private Request request;

        private Builder(){}

        public Builder withDefaults(){
            this.fileName = "testProcedures1.json";
            this.procedureName = "test1";
            return this;
        }
        public Builder withFileName(String fileName){
            this.fileName = fileName;
            return this;
        }
        public Builder withProcedure(String procedureName){
            this.procedureName = procedureName;
            return this;
        }

        public Builder withHandler(RequestHandler handler){
            this.handler = handler;
            return this;
        }

        public Builder withProcedureManager(){
            this.procedureManager = new ProcedureManager();
            return this;
        }

        public Initializator build(){
            return new Initializator(this);
        }

        public Builder withRequest(Request request){
            this.request = request;
            return this;
        }
    }
}