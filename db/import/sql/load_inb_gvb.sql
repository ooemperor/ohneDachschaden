TRUNCATE TABLE public.gvb_dangers CASCADE;

INSERT INTO public.gvb_dangers
SELECT NOW() AS Load_DTS,
       *
FROM stage.gvb_dangers;
