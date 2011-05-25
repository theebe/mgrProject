package pl.mgrProject.action;

import javax.ejb.Stateless;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.jboss.seam.international.StatusMessages;

@Stateless
@Name("Autocomplete")
public class AutocompleteBean implements Autocomplete
{
    @Logger private Log log;

    @In StatusMessages statusMessages;

    public void autocomplete()
    {
        // implement your business logic here
        log.info("Autocomplete.autocomplete() action called");
        statusMessages.add("autocomplete");
    }

    // add additional action methods

}
