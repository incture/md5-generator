package oneapp.incture.workbox.demo.versionControl.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.io.IOUtils;

import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;

import oneapp.incture.workbox.demo.versionControl.dto.AttachmentDto;

public class DocumentServiceUtil {
	/**
	 * @param encodedFile
	 * @param fileName
	 * @param fileType
	 * @return object id of newly created document, returns null if upload fails
	 */
	
	public static String uploadToDocumentService(byte[] encodedFile, String fileName, String fileType) {

		System.err.println("DocumentServiceUtil.uploadToDocumentService() attachment name: " + fileName + " type: " + fileType);

		String secretKey = "Key@Incture123$";
		Session openCmisSession = null;
		String uniqueName = "DemoVideos";
		Folder root = null;
		String objectId = null;
		try {
			 long start = System.currentTimeMillis();
			openCmisSession = connectToEcmService(secretKey, uniqueName);
			System.err.println("DocumentServiceUtil.uploadToDocumentService() connection done. " + "Connection time : "
					+ (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();
			root = openCmisSession.getRootFolder();
			System.err.println("DocumentServiceUtil.uploadToDocumentService() to get root folder time : "
					+ (System.currentTimeMillis() - start));
			
			// System.err.println("Got Root Folder. \nCreating Folder");
			// start=System.currentTimeMillis();
			
			creatFolder(root);
			
			// System.err.println("DocumentServiceUtil.uploadToDocumentService()
			// to create folder time : "+(System.currentTimeMillis()-start));
			
			System.err.println("creating document");
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
			fileName += System.currentTimeMillis();
			properties.put(PropertyIds.NAME, fileName);
			
			// start=System.currentTimeMillis();
			
			objectId = createDocument(openCmisSession, root, properties, encodedFile, fileName, fileType);
			
			// System.err.println("DocumentServiceUtil.uploadToDocumentService() to upload doc time : "+(System.currentTimeMillis()-start));
			// start=System.currentTimeMillis();
			// getRootChild(addAcl, root);
			// System.err.println("DocumentServiceUtil.uploadToDocumentService() to get child object time : "+(System.currentTimeMillis()-start));
			
			return objectId;
		} catch (Exception e) {
			System.err.println(
					"DocumentServiceUtil.uploadToDocumentService() error  : name " + fileName + " type " + fileType);
			e.printStackTrace();
		}

		return objectId;
	}

	// byte array content
	public static String uploadToDocumentService(String encodedFile, String fileName, String fileType) {

		System.err.println(
				"DocumentServiceUtil.uploadToDocumentService() attachment : name " + fileName + " type " + fileType);

		String secretKey = "Key@Incture123$";
		Session openCmisSession = null;
		String uniqueName = "DemoVideos";
		List<Ace> addAcl = null;
		Folder root = null;
		String objectId = null;
		try {
			openCmisSession = connectToEcmService(secretKey, uniqueName);
			System.err.println("Connection done");

			root = openCmisSession.getRootFolder();
			System.err.println("got root Folder");
			System.err.println("Creating Folder");

			creatFolder(root);
			System.err.println("creating document");

			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
			fileName += System.currentTimeMillis();
			properties.put(PropertyIds.NAME, fileName);

			objectId = createDocument(openCmisSession, root, properties, encodedFile, fileName, fileType);

			getRootChild(addAcl, root);

			return objectId;

		} catch (Exception e) {
			System.err.println(
					"DocumentServiceUtil.uploadToDocumentService() error  : name " + fileName + " type " + fileType);
			e.printStackTrace();
		}

		return objectId;
	}

	private static Session connectToEcmService(String secretKey, String uniqueName) throws NamingException {

		Session openCmisSession = null;
		EcmService ecmSvc = null;
		InitialContext ctx = null;
		String lookupName = null;

		try {
			ctx = new InitialContext();
			lookupName = "java:comp/env/EcmService";
			ecmSvc = (EcmService) ctx.lookup(lookupName);
			// connect to my repository
			openCmisSession = ecmSvc.connect(uniqueName, secretKey);

		} catch (CmisObjectNotFoundException e) {
			// repository does not exist, so try to create it
			RepositoryOptions options = new RepositoryOptions();
			options.setUniqueName(uniqueName);
			options.setRepositoryKey(secretKey);
			options.setVisibility(Visibility.PROTECTED);
			ecmSvc.createRepository(options);

			openCmisSession = ecmSvc.connect(uniqueName, secretKey);
		} catch (Exception exception) {
			System.err.println("DocumentServiceUtil.connectToEcmService() error : " + exception);
		}

		return openCmisSession;
	}

	private static void creatFolder(Folder root) {
		try {

			System.err.println("Creating folder");
			// create a new folder
			Map<String, String> newFolderProps = new HashMap<>();
			newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
			newFolderProps.put(PropertyIds.NAME, "SPDocuments");
			root.createFolder(newFolderProps);
			System.err.println("Folder creation done");
		} catch (CmisNameConstraintViolationException e) {
			System.err.println("Folder already exists");
		} catch (Exception e) {
			System.err.println("Error while creating folder");
			e.printStackTrace();
		}
	}

	private static void getRootChild(List<Ace> addAcl, Folder root) {
		try {
			ItemIterable<CmisObject> children = root.getChildren();
			System.err.println("no of root children: " + children.getTotalNumItems());
			for (CmisObject o : children) {
				if (o instanceof Folder) {
					// System.err.println("content " + o.getCreatedBy());
					// System.err.println("Folder recieved: " + o.getName());
				} else {
					Document doc = (Document) o;
					System.err.println("Document created by : " + doc.getCreatedBy() + " Object id : " + doc.getId());
				}
			}
		} catch (Exception e) {
			System.err.println("Error in listing content");
			e.printStackTrace();
		}
	}

	private static String createDocument(Session openCmisSession, Folder root, Map<String, Object> properties,
			String encodedFile, String fileName, String fileType) {
		String objectId = null;
		try {
			byte[] fileContent = Base64.getDecoder().decode(encodedFile);
			InputStream stream = new ByteArrayInputStream(fileContent);
			ContentStream contentStream = openCmisSession.getObjectFactory().createContentStream(fileName,
					fileContent.length, fileType + ";base64", stream);
			Document newDoc = root.createDocument(properties, contentStream, VersioningState.NONE);
			System.err.println("Document creation done with id: " + newDoc.getId());
			objectId = newDoc.getId();
			return objectId;
		} catch (Exception e) {
			System.err.println("error while file creation");
			e.printStackTrace();
		}
		return objectId;
	}

	private static String createDocument(Session openCmisSession, Folder root, Map<String, Object> properties,
			byte[] encodedFile, String fileName, String fileType) {
		String objectId = null;
		try {
			byte[] fileContent = encodedFile;
			InputStream stream = new ByteArrayInputStream(fileContent);
			ContentStream contentStream = openCmisSession.getObjectFactory().createContentStream(fileName,
					fileContent.length, fileType + ";base64", stream);
			Document newDoc = root.createDocument(properties, contentStream, VersioningState.NONE);
			System.err.println("Document creation done with id: " + newDoc.getId());
			objectId = newDoc.getId();
			return objectId;
		} catch (Exception e) {
			System.err.println("error while file creation");
			e.printStackTrace();
		}
		return objectId;
	}

	@SuppressWarnings("unused")
	public static AttachmentDto getOriginalDocument(String objectId) 
	{
		String lookupName = "java:comp/env/EcmService";
		AttachmentDto attachment = new AttachmentDto();
		try {
			InitialContext ctx = new InitialContext();
			Session openCmisSession = connectToEcmService("Key@Incture123$", "DemoVideos");
			System.err.println("Connection done");
			Folder root = openCmisSession.getRootFolder();
			Document document = (Document) openCmisSession.getObject(objectId);

			if (null != document) {
				Property<String> p = document.getProperty(PropertyIds.NAME);
				InputStream stream = document.getContentStream().getStream();		
				byte[] originalDoc = IOUtils.toByteArray(stream);
				byte[] encoded = Base64.getEncoder().encode(originalDoc);
//				attachment.setFile(originalDoc);
				attachment.setEncodedFileContent(new String(encoded));
				attachment.setFileName(document.getContentStreamFileName());
				attachment.setFileType(document.getContentStreamMimeType());
				return attachment;
			}
			return attachment;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("Repo connection failed");
		}
		return attachment;
	}

}