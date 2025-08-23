TRUNCATE TABLE public.gvb_danger CASCADE;

INSERT INTO public.gvb_dangers
SELECT NOW() AS Load_DTS,
       *
FROM stage.gvb_dangers;
