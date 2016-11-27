package com.wangsheng.httplibrary.http;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * Created by liyaming on 16/3/9.
 * form 表单序列化使用
 */
public class FormDataEntity implements Parcelable
{

	/**
	 * 表单名称
	 */
	private String formName;

	/**
	 * 表单值
	 */
	private String formValue;

	protected FormDataEntity() {

	}

	public FormDataEntity(String formName, String formValue)
	{
		this.formName = formName;
		this.formValue = formValue;
	}

	public String getFormValue()
	{
		return formValue;
	}

	public void setFormValue(String formValue)
	{
		this.formValue = formValue;
	}

	public String getFormName()
	{
		return formName;
	}

	public void setFormName(String formName)
	{
		this.formName = formName;
	}

	@Override
	public String toString()
	{
		return "FormDataEntity{"+
				"formName='"+formName+'\''+
				", formValue='"+formValue+'\''+
				'}';
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(formName);
		dest.writeString(formValue);
	}

	public static final Parcelable.Creator<FormDataEntity> CREATOR = new Parcelable.Creator<FormDataEntity>() {

		@Override
		public FormDataEntity createFromParcel(Parcel source)
		{
			return new FormDataEntity(source);
		}

		@Override
		public FormDataEntity[] newArray(int size)
		{
			return new FormDataEntity[size];
		}
	};

	private FormDataEntity(Parcel in) {
		this.formName = in.readString();
		this.formValue = in.readString();
	}

	/**
	 * 表单数组创建，不现式的给出FormDataEntity的构造函数
	 */
	public static final class FormListBuilder {

		private final ArrayList<FormDataEntity> list = new ArrayList<>();

		/**
		 * 添加表单数据
		 * @param formName 表单名称（不为空）
		 * @param formValue 表单值（不为空）
		 * @return builder 对象
		 */
		public FormListBuilder addFormData(final String formName, final String formValue) {
			list.add(new FormDataEntity(formName,formValue));
			return this;
		}

		public ArrayList<FormDataEntity> build() {
			return list;
		}
	}
}
