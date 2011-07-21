package pl.mgrProject.action.utils;

import java.util.StringTokenizer;

import javax.faces.component.UIComponent;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.postgis.Point;

@Name("pointConverter")
@BypassInterceptors
@Converter
public class PointConverter implements javax.faces.convert.Converter {

	@Override
	@Transactional
	public Object getAsObject(javax.faces.context.FacesContext arg0,
			UIComponent arg1, String arg2) {

		
		Point ret = null;

		if(arg2 == null && arg2.isEmpty())
			return ret;
		
		String[] p = arg2.split(", ");
		ret = new Point(Double.valueOf(p[0]), Double.valueOf(p[1]));
				
		return ret;
	}

	@Override
	public String getAsString(javax.faces.context.FacesContext arg0,
			UIComponent arg1, Object arg2) {
		String ret = null;

		if (arg2 != null) {
			Point point = (Point) arg2;
			ret = new String(String.valueOf(point.getX()) + ", "
					+ String.valueOf(point.getY()));
		}
		return ret;
	}

}