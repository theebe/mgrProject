package pl.mgrProject.action.rest.message;

import static pl.mgrProject.action.rest.message.MessageBuilderHelper.NL;
import static pl.mgrProject.action.rest.message.MessageBuilderHelper.TAB;

import java.util.Stack;

public abstract class AbstractMessageBuilder implements MessageBuilder {

	protected Stack<String> opens = new Stack<String>();
	protected boolean prettyPrint;

	public AbstractMessageBuilder(boolean pp) {
		this.prettyPrint = pp;
	}

	/**
	 * Maluje enter i tabulatory
	 * 
	 * @param sb
	 */
	protected void tabs(final StringBuilder sb) {
		if (prettyPrint) {
			sb.append(NL);
			opens.trimToSize();
			for (int i = 0; i < opens.size(); ++i)
				sb.append(TAB);
		}
	}

}
