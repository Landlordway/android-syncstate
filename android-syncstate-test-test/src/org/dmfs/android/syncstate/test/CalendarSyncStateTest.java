package org.dmfs.android.syncstate.test;

import java.io.IOException;

import org.dmfs.android.syncstate.CalendarSyncState;
import org.dmfs.android.syncstate.SyncState;
import org.dmfs.xmlobjects.ElementDescriptor;
import org.dmfs.xmlobjects.QualifiedName;
import org.dmfs.xmlobjects.XmlContext;
import org.dmfs.xmlobjects.builder.StringObjectBuilder;

import android.accounts.Account;
import android.os.RemoteException;
import android.provider.CalendarContract;
import android.test.AndroidTestCase;


/**
 * Test {@link CalendarSyncState}.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class CalendarSyncStateTest extends AndroidTestCase
{

	/**
	 * Test descriptor that we add to the sync state.
	 */
	private final static ElementDescriptor<String> ELEMENT1 = ElementDescriptor.register(QualifiedName.get("http://dmfs.org/ns/1", "element1"),
		StringObjectBuilder.INSTANCE);

	/**
	 * Another Test descriptor that we add to the sync state.
	 */
	private final static ElementDescriptor<String> ELEMENT2 = ElementDescriptor.register(QualifiedName.get("http://dmfs.org/ns/2", "element2"),
		StringObjectBuilder.INSTANCE);

	/**
	 * An {@link XmlContext}.
	 */
	private final static XmlContext CONTEXT = new XmlContext();

	/**
	 * Test descriptor that we add to the sync state. This element is not in the default context.
	 */
	private final static ElementDescriptor<String> CONTEXT_ELEMENT1 = ElementDescriptor.register(QualifiedName.get("http://dmfs.org/ns/1", "context_element1"),
		StringObjectBuilder.INSTANCE, CONTEXT);

	/**
	 * Another Test descriptor that we add to the sync state. This element is not in the default context.
	 */
	private final static ElementDescriptor<String> CONTEXT_ELEMENT2 = ElementDescriptor.register(QualifiedName.get("http://dmfs.org/ns/2", "context_element2"),
		StringObjectBuilder.INSTANCE, CONTEXT);


	public void testCalendarSyncState() throws IOException, RemoteException
	{
		Account testAccount = new Account("test", CalendarContract.ACCOUNT_TYPE_LOCAL);

		// create a new CalendarSyncState for the test account
		SyncState s = new CalendarSyncState(getContext().getContentResolver(), testAccount)
		{
		};

		// the values must not exist yet
		assertNull(s.get(ELEMENT1));
		assertNull(s.get(ELEMENT2));

		// add two values
		s.set(ELEMENT1, "some string value");
		s.set(ELEMENT2, "some other string value");

		// check that the values are returned
		assertEquals("some string value", s.get(ELEMENT1));
		assertEquals("some other string value", s.get(ELEMENT2));

		// store the sync state
		s.store();

		// make sure that the values are still returned correctly
		assertEquals("some string value", s.get(ELEMENT1));
		assertEquals("some other string value", s.get(ELEMENT2));

		// create a new CalendarSyncState
		SyncState s2 = new CalendarSyncState(getContext().getContentResolver(), testAccount)
		{
		};

		// ensure it doesn't contain any values yet
		assertNull(s2.get(ELEMENT1));
		assertNull(s2.get(ELEMENT2));

		// load the sync state
		s2.load();

		// make sure that the values are still returned correctly
		assertEquals("some string value", s2.get(ELEMENT1));
		assertEquals("some other string value", s2.get(ELEMENT2));
	}


	public void testCalendarSyncStateWithContext() throws IOException, RemoteException
	{
		Account testAccount = new Account("test2", CalendarContract.ACCOUNT_TYPE_LOCAL);

		// create a new CalendarSyncState for the test account
		SyncState s = new CalendarSyncState(getContext().getContentResolver(), testAccount)
		{
		};

		// the values must not exist yet
		assertNull(s.get(CONTEXT_ELEMENT1));
		assertNull(s.get(CONTEXT_ELEMENT2));

		// add two values
		s.set(CONTEXT_ELEMENT1, "some string value");
		s.set(CONTEXT_ELEMENT2, "some other string value");

		// check that the values are returned
		assertEquals("some string value", s.get(CONTEXT_ELEMENT1));
		assertEquals("some other string value", s.get(CONTEXT_ELEMENT2));

		// store the sync state
		s.store(CONTEXT);

		// make sure that the values are still returned correctly
		assertEquals("some string value", s.get(CONTEXT_ELEMENT1));
		assertEquals("some other string value", s.get(CONTEXT_ELEMENT2));

		// create a new CalendarSyncState
		SyncState s2 = new CalendarSyncState(getContext().getContentResolver(), testAccount)
		{
		};

		// ensure it doesn't contain any values yet
		assertNull(s2.get(CONTEXT_ELEMENT1));
		assertNull(s2.get(CONTEXT_ELEMENT2));

		// load the sync state
		s2.load(CONTEXT);

		// make sure that the values are still returned correctly
		assertEquals("some string value", s2.get(CONTEXT_ELEMENT1));
		assertEquals("some other string value", s2.get(CONTEXT_ELEMENT2));
	}
}
