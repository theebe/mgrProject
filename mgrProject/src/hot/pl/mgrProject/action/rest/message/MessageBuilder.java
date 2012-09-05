package pl.mgrProject.action.rest.message;

import java.util.List;

import pl.mgrProject.action.Odpowiedz;
import pl.mgrProject.model.Linia;
import pl.mgrProject.model.Przystanek;

public interface MessageBuilder {

	public String buildMessage(String s);
	
	public String buildMessage(Odpowiedz o);
	
	public String buildMessagePrzystanek(List<Przystanek> lp, String... include);
	
	public String buildMessageLinia(List<Linia> ll, String... include);
	
	
}
