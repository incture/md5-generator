package oneapp.incture.workbox.demo.test.config;

import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
//@EnableScheduling
@EnableWebSocket
@ComponentScan(basePackages = {"oneapp.incture.workbox.demo"})
public class SpringConfiguration implements WebSocketConfigurer{

	//	@Bean(name="PersistDataService")
	//	public PersistDataService getPersistDataService() {
	//		return new PersistDataService();
	//	}

	@Bean
	public ThreadPoolTaskScheduler taskScheduler(TaskSchedulerBuilder builder) {
	    return builder.build();
	}
	
	@Bean
	public ObjectMapper mapper() {
		return new ObjectMapper();
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		//registry.addHandler(unitWebSocketHandler, "/unit").setAllowedOrigins("*");
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
