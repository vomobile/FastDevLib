package com.wangsheng.httplibrary.http;

import android.os.Parcel;
import android.os.Parcelable;

import com.wangsheng.httplibrary.util.FileUtils;

import java.io.IOException;
import java.util.ArrayList;


/**
 * 带文件上传的表单
 */
public class FileFormDataEntity extends FormDataEntity
{
	private static final String LOG_TAG = "FileFormDataEntity";

	/**
	 * 文件路径
	 */
	private String filePath;

	private FileFormDataEntity(String formname, String filePath){

		this.setFormName(formname);
		this.filePath = filePath;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(getFormName());
		dest.writeString(filePath);

	}

	private FileFormDataEntity(Parcel in) {
		setFormName(in.readString());
		filePath = in.readString();
	}

	public static final Parcelable.Creator<FileFormDataEntity> CREATOR = new Parcelable.Creator<FileFormDataEntity>() {

		@Override
		public FileFormDataEntity createFromParcel(Parcel source)
		{
			return new FileFormDataEntity(source);
		}

		@Override
		public FileFormDataEntity[] newArray(int size)
		{
			return new FileFormDataEntity[size];
		}
	};

	/**
	 * 表单数组创建，不现式的给出FormDataEntity的构造函数
	 */
	public static final class FileFormListBuilder {

		private final ArrayList<FormDataEntity> list = new ArrayList<>();

		/**
		 * 添加表单数据
		 * @param formName 表单名称（不为空）
		 * @param formValue 表单值（不为空）
		 * @return builder 对象
		 */
		public FileFormListBuilder addFormData(final String formName, final String formValue) {
			list.add(new FormDataEntity(formName,formValue));
			return this;
		}

		/**
		 * 添加文件数据
		 * @param formName 字段名称
		 * @param fileFath 文件路径（文件必须存在）
		 * @return builder 对象
		 */
		public FileFormListBuilder addFileFormData(final String formName, final String fileFath) throws IOException {
			if(!FileUtils.exists(fileFath)) {
				throw new IOException("文件不存在");
			}
			list.add(new FileFormDataEntity(formName,fileFath));
			return this;
		}

		public ArrayList<FormDataEntity> build() {
			return list;
		}
	}
}
