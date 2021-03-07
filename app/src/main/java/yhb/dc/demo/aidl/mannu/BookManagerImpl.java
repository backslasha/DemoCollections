package yhb.dc.demo.aidl.mannu;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhb on 18-4-24.
 */

public class BookManagerImpl extends Binder implements IBookManger {

    private List<Book> mBooks;

    public BookManagerImpl() {
        this.attachInterface(this, IBookManger.DESCRIPTION);
        this.mBooks = new ArrayList<>();
    }

    public static IBookManger asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IInterface iInterface = obj.queryLocalInterface(IBookManger.DESCRIPTION);
        if (iInterface != null && iInterface instanceof IBookManger) {
            return (IBookManger) iInterface;
        }

        return new BookManagerImpl.Proxy(obj);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        Log.d(getClass().getSimpleName(), "Books: " + mBooks.toString());
        return mBooks;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        if (book != null) {
            mBooks.add(book);
            Log.d(getClass().getSimpleName(), "add book: " + book);
        }
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(IBookManger.DESCRIPTION);
                return true;
            case IBookManger.TRANSACTION_getBookList:
                data.enforceInterface(IBookManger.DESCRIPTION);
                List<Book> result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            case IBookManger.TRANSACTION_addBook:
                data.enforceInterface(IBookManger.DESCRIPTION);
                Book book;
                if ((0 != data.readInt())) {
                    book = Book.CREATOR.createFromParcel(data);
                } else {
                    book = null;
                }
                this.addBook(book);
                reply.writeNoException();
                return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    public static class Proxy implements IBookManger {
        private IBinder mRemote;

        public Proxy(IBinder remote) {
            mRemote = remote;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result;
            try {
                data.writeInterfaceToken(IBookManger.DESCRIPTION);
                mRemote.transact(IBookManger.TRANSACTION_getBookList, data, reply, 0);
                reply.readException();
                result = reply.createTypedArrayList(Book.CREATOR);
            } finally {
                reply.recycle();
                data.recycle();
            }

            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                if (book != null) {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                mRemote.transact(IBookManger.TRANSACTION_addBook, data, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                data.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public String getDescription() {
            return IBookManger.DESCRIPTION;
        }
    }

}
