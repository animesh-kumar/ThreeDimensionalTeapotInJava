package teapot;

import javax.media.j3d.Shape3D;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

public class TeapotSurface extends Shape3D {
	public TeapotSurface(Point3d[] pts, int[] idx) {
		GeometryInfo gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
		gi.setCoordinates(pts);
		gi.setCoordinateIndices(idx);

		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(gi);
		this.setGeometry(gi.getGeometryArray());
	}

}
