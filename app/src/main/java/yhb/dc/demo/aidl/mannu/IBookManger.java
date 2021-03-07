package yhb.dc.demo.aidl.mannu;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

/**
 * Created by yhb on 18-4-24.
 */

public interface IBookManger extends IInterface {
    String DESCRIPTION = "yhb.dc.demo.aidl.IBookManager";
    int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION;
    int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

    List<Book> getBookList() throws RemoteException;

    void addBook(Book book) throws RemoteException;
}
