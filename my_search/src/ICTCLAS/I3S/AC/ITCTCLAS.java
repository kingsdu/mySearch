package ICTCLAS.I3S.AC;

import java.io.File;


public class ITCTCLAS
{   
//	public static void main(String[] args)
//	{      
//		String sInput = "随后温总理就离开了舟曲县城，预计温总理今天下午就回到北京。以上就是今天上午的最新动态,性价比很高";
//		System.out.println(ICTCLAS_ParagraphProcess(sInput));
//		String argu = new File("").getAbsolutePath()+"\\bin";
//		System.out.println(argu);
//	}



	public static String ICTCLAS_ParagraphProcess(String sInput)
	{
		try
		{
			ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
			String argu = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			argu = argu.substring(1,argu.lastIndexOf("/"));
			if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("GB2312")) == false)
			{
				System.out.println("Init Fail!");
				return null;
			}

			testICTCLAS50.ICTCLAS_SetPOSmap(2);

			byte nativeBytes[] = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("GB2312"), 0, 1);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
			testICTCLAS50.ICTCLAS_SaveTheUsrDic();
			testICTCLAS50.ICTCLAS_Exit();
			return nativeStr;
		}
		catch (Exception ex)
		{
		}
		return sInput;

	}



	public static void testICTCLAS_FileProcess()
	{
		try
		{
			ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
			String argu = ".";
			if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("GB2312")) == false)
			{
				System.out.println("Init Fail!");
				return;
			}

			String Inputfilename = "test.txt";
			byte[] Inputfilenameb = Inputfilename.getBytes();

			String Outputfilename = "test_result.txt";
			byte[] Outputfilenameb = Outputfilename.getBytes();

			testICTCLAS50.ICTCLAS_FileProcess(Inputfilenameb, 0, 0, Outputfilenameb);

			int nCount = 0;
			String usrdir = "userdict.txt"; 
			byte[] usrdirb = usrdir.getBytes();
			nCount = testICTCLAS50.ICTCLAS_ImportUserDictFile(usrdirb, 0);
			System.out.println("导入用户词个数" + nCount);
			nCount = 0;

			String Outputfilename1 = "testing_result.txt";
			byte[] Outputfilenameb1 = Outputfilename1.getBytes();

			testICTCLAS50.ICTCLAS_FileProcess(Inputfilenameb, 0, 0, Outputfilenameb1);
		}
		catch (Exception ex)
		{
		}

	}

}





