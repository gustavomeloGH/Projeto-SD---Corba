package br.com.novaroma.Server;

import br.com.novaroma.ReverseApp.*;

import javax.swing.JOptionPane;

import org.omg.CORBA.*;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class StartServer {

	public static void main(String args[]) {
		try {
			// create and initialize the ORB //// get reference to rootpoa &amp; activate
			// the POAManager
			ORB orb = ORB.init(args, null);
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// create servant and register it with the ORB
			ReverseObj reverseObj = new ReverseObj();
			reverseObj.setORB(orb);

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(reverseObj);
			Reverse href = ReverseHelper.narrow(ref);

			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			NameComponent path[] = ncRef.to_name("ABC");
			ncRef.rebind(path, href);

			System.out.println("Reverse Server ready and waiting ...");

			// wait for invocations from clients
			while (true) {
				orb.run();
			}
		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(null, ("ERROR: " + e));
			e.printStackTrace(System.out);
		}

	}
	//abrir isso: -ORBInitialPort 1050 -ORBInitialHost localhost
	//start no orbd-> start orbd -ORBInitialPort 1050
}
