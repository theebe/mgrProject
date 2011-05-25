package pl.mgrProject.action;

import javax.ejb.Local;

@Local
public interface Autocomplete
{
    // seam-gen method
    public void autocomplete();

    // add additional interface methods here

}
