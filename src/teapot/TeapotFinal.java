package teapot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Material;
import javax.media.j3d.PointLight;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JRadioButton;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class TeapotFinal extends JFrame implements ActionListener {
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = -7749992733665717921L;
	private int n;
	int[] idx = null;
	Point3d[] pts = null;
	private JMenuBar menuBar;
	private JRadioButton topViewButton;
	private JRadioButton sideViewButton;
	private JRadioButton frontViewButton;
	private boolean topView;
	private boolean sideView;
	private boolean frontView;
	public static String[] filenames = { "data/tpot1.dat",
			"data/tpot2.dat", "data/tpot3.dat",
			"data/tpot4.dat", "data/tpot5.dat",
			"data/tpot6.dat", "data/tpot7.dat", "data/tpot8.dat" };

	public static void main(String[] args) {
		new TeapotFinal(false, true, false);
	}

	private void createOptions(boolean topViewSelection,
			boolean sideViewSelection, boolean frontViewSelection) {
		menuBar = new JMenuBar(); // somewhere to put the master controls
		setJMenuBar(menuBar);

		topViewButton = new JRadioButton("Top View");
		topViewButton.addActionListener(this);
		topViewButton.setSelected(topViewSelection);
		menuBar.add(topViewButton);
		topView = topViewSelection;

		sideViewButton = new JRadioButton("Side View");
		sideViewButton.addActionListener(this);
		sideViewButton.setSelected(sideViewSelection);
		menuBar.add(sideViewButton);
		sideView = sideViewSelection;

		frontViewButton = new JRadioButton("Front View");
		frontViewButton.addActionListener(this);
		frontViewButton.setSelected(frontViewSelection);
		menuBar.add(frontViewButton);
		frontView = frontViewSelection;
	}

	public TeapotFinal(boolean topViewSelection, boolean sideViewSelection,
			boolean frontViewSelection) {
		createOptions(topViewSelection, sideViewSelection, frontViewSelection);
		createScene();

	}

	private void createScene() {
		// Mechanism for closing the window and ending the program.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Default settings for the viewer parameters.
		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
		Canvas3D cv = new Canvas3D(gc);
		setLayout(new BorderLayout());
		add(cv, BorderLayout.CENTER);

		// Construct the SimpleUniverse:
		// First generate it using the Canvas.
		SimpleUniverse su = new SimpleUniverse(cv);
		// Default position of the viewer.
		su.getViewingPlatform().setNominalViewingTransform();

		// The scene is generated in this method.
		BranchGroup bg = createSceneGraph();
		bg.compile();
		su.addBranchGraph(bg);

		// Show the canvas/window.
		setTitle("Teapot 3D");
		setSize(700, 700);
		getContentPane().add("Center", cv);
		setVisible(true);
	}

	/**
	 * In this method, the objects for the scene are generated and added to the
	 * SimpleUniverse.
	 * 
	 * @return
	 */
	private BranchGroup createSceneGraph() {
		BranchGroup root = new BranchGroup();
		TransformGroup spin = new TransformGroup();
		spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		root.addChild(spin);

		// surface
		Appearance ap = new Appearance();
		ap.setMaterial(new Material());
		// Top View Requires Two Rotations, one along Y axis and another along X
		// axis
		if (topView) {
			Transform3D tr = new Transform3D();
			tr.rotY(Math.PI * 1);
			Transform3D tr1 = new Transform3D();
			tr1.rotX(Math.PI);
			tr.mul(tr1);
			tr.setScale(0.20);
			tr.setTranslation(new Vector3d(0, 0, 0));
			TransformGroup tg = new TransformGroup(tr);
			spin.addChild(tg);
			createTeapot(ap, tg);
		} else if (frontView) {// Front View Requires Three Rotations, one along
								// X axis, another along Y axis and the third
								// again along X
			// axis
			Transform3D tr = new Transform3D();
			tr.rotX(Math.PI);
			Transform3D tr1 = new Transform3D();
			tr1.rotY(Math.PI * 0.5);
			tr.mul(tr1);
			Transform3D tr2 = new Transform3D();
			tr2.rotX(Math.PI * 0.5);
			tr.mul(tr2);
			tr.setScale(0.25);
			tr.setTranslation(new Vector3d(0, -0.5, 0));
			TransformGroup tg = new TransformGroup(tr);
			spin.addChild(tg);
			createTeapot(ap, tg);
		} else if (sideView) {// Side View Requires Two Rotations, one along X
								// axis and another along Y axis
			Transform3D tr = new Transform3D();
			tr.rotX(Math.PI * 0.5);
			Transform3D tr1 = new Transform3D();
			tr1.rotY(Math.PI);
			tr.mul(tr1);
			tr.setScale(0.25);
			tr.setTranslation(new Vector3d(0, -0.4, 0));
			TransformGroup tg = new TransformGroup(tr);
			spin.addChild(tg);
			createTeapot(ap, tg);
		}
		BoundingSphere bounds = new BoundingSphere();
		bounds.setRadius(10);

		return addLight(root, bounds);
	}

	/**
	 * Add Some Light to the Scene Here.
	 * @param root
	 * @param bounds
	 * @return
	 */
	private BranchGroup addLight(BranchGroup root, BoundingSphere bounds) {
		// background and lights
		Background background = new Background(1f, 1f, 1f);
		background.setApplicationBounds(bounds);
		root.addChild(background);
		AmbientLight light = new AmbientLight(true, new Color3f(Color.white));
		light.setInfluencingBounds(bounds);
		root.addChild(light);
		PointLight ptlight = new PointLight(new Color3f(Color.white),
				new Point3f(0.7f, 1.8f, 1.8f), new Point3f(1f, 0.2f, 0f));
		ptlight.setInfluencingBounds(bounds);
		root.addChild(ptlight);
		return root;
	}

	/**
	 * Creates the Teapot using the files specifying the coordinates
	 * 
	 * @param ap
	 * @param tg
	 */
	private void createTeapot(Appearance ap, TransformGroup tg) {
		String classPath = System.getProperty("user.dir");
		
		createTeapotPart(classPath + "/" + filenames[0]);

		// Create a 3d shape using the co-ordinates from the file and add the
		// shape to the scene
		Shape3D shape1 = new TeapotSurface(pts, idx);
		shape1.setAppearance(ap);
		tg.addChild(shape1);
		createTeapotPart(classPath + "/" + filenames[1]);
		Shape3D shape2 = new TeapotSurface(pts, idx);
		shape2.setAppearance(ap);
		tg.addChild(shape2);
		createTeapotPart(classPath + "/" + filenames[2]);
		Shape3D shape3 = new TeapotSurface(pts, idx);
		shape3.setAppearance(ap);
		tg.addChild(shape3);
		createTeapotPart(classPath + "/" + filenames[3]);
		Shape3D shape4 = new TeapotSurface(pts, idx);
		shape4.setAppearance(ap);
		tg.addChild(shape4);
		createTeapotPart(classPath + "/" + filenames[4]);
		Shape3D shape5 = new TeapotSurface(pts, idx);
		shape5.setAppearance(ap);
		tg.addChild(shape5);
		createTeapotPart(classPath + "/" + filenames[5]);
		Shape3D shape6 = new TeapotSurface(pts, idx);
		shape6.setAppearance(ap);
		tg.addChild(shape6);
		createTeapotPart(classPath + "/" + filenames[6]);
		Shape3D shape7 = new TeapotSurface(pts, idx);
		shape7.setAppearance(ap);
		tg.addChild(shape7);
		createTeapotPart(classPath + "/" + filenames[7]);
		Shape3D shape8 = new TeapotSurface(pts, idx);
		shape8.setAppearance(ap);
		tg.addChild(shape8);
	}

	private void createTeapotPart(String filename) {
		try {
			FileInputStream fs = new FileInputStream(filename);

			DataInputStream in = new DataInputStream(fs);

			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line = br.readLine();
			n = Integer.parseInt(line);
			// Reads the first 64 points from the files as 3d Points
			pts = new Point3d[n];
			for (int i = 0; i < n; i++) {
				line = br.readLine();
				StringTokenizer st = new StringTokenizer(line, "\t \n");
				Double.parseDouble(st.nextToken());
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());
				double z = Double.parseDouble(st.nextToken());
				pts[i] = new Point3d(x, y, z);
			}
			line = br.readLine();
			if (Integer.parseInt(line) == 1) {
				line = br.readLine();
				// The next 36 lines represent the Rectangles
				int noOfRectangles = Integer.parseInt(line);
				idx = new int[noOfRectangles * 4];
				int j = 0;
				for (int i = 0; i < noOfRectangles; i++) {
					line = br.readLine();
					StringTokenizer st = new StringTokenizer(line, "\t \n");
					Integer.parseInt(st.nextToken());// Represents the count
														// of rectangle
														// reached
					Integer.parseInt(st.nextToken());// Represents that,
														// next defined is
														// one rectangle
					Integer.parseInt(st.nextToken());// Represents the no of
														// coordinates for the
														// rectangle
					int i1 = Integer.parseInt(st.nextToken());
					idx[j++] = i1 - 1;
					int i2 = Integer.parseInt(st.nextToken());
					idx[j++] = i2 - 1;
					int i3 = Integer.parseInt(st.nextToken());
					idx[j++] = i3 - 1;
					int i4 = Integer.parseInt(st.nextToken());
					idx[j++] = i4 - 1;
				}
			}
			br.close();
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == topViewButton) {
			topView = topViewButton.isSelected();
			if (topView) {
				sideViewButton.setSelected(false);
				frontViewButton.setSelected(false);
				new TeapotFinal(true, false, false);
			}
		} else if (ev.getSource() == sideViewButton) {
			sideView = sideViewButton.isSelected();
			if (sideView) {
				topViewButton.setSelected(false);
				frontViewButton.setSelected(false);
				new TeapotFinal(false, true, false);
			}
		} else if (ev.getSource() == frontViewButton) {
			frontView = frontViewButton.isSelected();
			if (frontView) {
				sideViewButton.setSelected(false);
				topViewButton.setSelected(false);
				new TeapotFinal(false, false, true);
			}
		}

	}

}
